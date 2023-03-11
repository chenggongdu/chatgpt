package com.dcg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/wx/msg/{appid}")
public class WxMpPortalController {

    private final WxMpService wxService;
    private final WxMpMessageRouter messageRouter;
    private Map<Long, String> msgMap = new HashMap<>();

    /**
     * 微信服务器的认证消息
     * 公众号接入开发模式时腾讯调用此接口
     * 参考文档 https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@PathVariable String appid,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
                timestamp, nonce, echostr);
        this.wxService.switchoverTo(appid);

        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    /**
     * 微信各类消息
     * 公众号接入开发模式后才有效
     * 参考文档 https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        this.wxService.switchoverTo(appid);
        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
        log.info("inMessge=" + inMessage);

        // 处理微信5秒不响应超时问题
        // 代码控制到最多处理时间为15秒
        WxMpXmlOutMessage outMessage;
        if (!msgMap.containsKey(inMessage.getMsgId())) {
            msgMap.put(inMessage.getMsgId(), null);
            outMessage = this.route(appid, inMessage);
            msgMap.put(inMessage.getMsgId(), outMessage.toXml());
        }
        while (msgMap.containsKey(inMessage.getMsgId())) {
            if (msgMap.get(inMessage.getMsgId()) != null) {
                break;
            }
        }
        return msgMap.get(inMessage.getMsgId());
    }


    private WxMpXmlOutMessage route(String appid, WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(appid, message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }
        return null;
    }


    @Scheduled(cron ="0 */10 * * * ?")
    public void clearMsgMap() {
        log.info("clearMsgMap = {}", msgMap);
        msgMap.clear();
    }



}
