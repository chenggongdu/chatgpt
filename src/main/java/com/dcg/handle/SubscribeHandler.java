package com.dcg.handle;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.util.WxMpConfigStorageHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SubscribeHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {

        log.info("新关注用户 OPENID: " + wxMessage.getFromUser() + "，事件：" + wxMessage.getEventKey());
        String appid = WxMpConfigStorageHolder.get();
        log.info("appid:{}", appid);
        return WxMpXmlOutMessage.TEXT().content("谢谢关注！现在你可以与GPT对话了哈~").fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser()).build();
    }
}
