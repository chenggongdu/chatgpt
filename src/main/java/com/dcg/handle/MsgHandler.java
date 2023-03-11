package com.dcg.handle;


import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.common.Choice;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang
 */
@Slf4j
@Component
@AllArgsConstructor
public class MsgHandler extends AbstractHandler {
    private OpenAiClient openAiClient;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        log.info("MsgHandler");
        String textContent = wxMessage.getContent();
        Choice[] choices = openAiClient.completions(textContent).getChoices();
        StringBuilder sb = new StringBuilder();
        for (Choice choice : choices) {
            sb.append(choice.getText());
        }
        return WxMpXmlOutMessage.TEXT().content(sb.toString().trim()).fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser()).build();

    }

}
