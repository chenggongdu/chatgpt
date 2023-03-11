package com.dcg.handle;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.util.WxMpConfigStorageHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MenuHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {
        if (WxConsts.EventType.VIEW.equals(wxMessage.getEvent())) {
            return null;
        }
        String appid = WxMpConfigStorageHolder.get();
        log.info("菜单事件：" + wxMessage.getEventKey());
        return null;
    }


}
