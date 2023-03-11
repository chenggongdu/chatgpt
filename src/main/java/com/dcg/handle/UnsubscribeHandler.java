package com.dcg.handle;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class UnsubscribeHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        String openid = wxMessage.getFromUser();
        log.info("取消关注用户 OPENID: " + openid);
        return WxMpXmlOutMessage.TEXT().content("下次再见！").fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser()).build();
    }

}
