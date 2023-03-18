package com.dcg.handle;


import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.common.Choice;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.List;
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
        String content = fetchContentByChatGpt3_5(wxMessage);
//        String content = fetchContentByChatGptText_davinci_003(wxMessage);
        return WxMpXmlOutMessage.TEXT().content(content.trim()).fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser()).build();
    }

    // 采用 test-davince-003模型
    private String fetchContentByChatGptText_davinci_003(WxMpXmlMessage wxMessage) {
        String textContent = wxMessage.getContent();
        Choice[] choices = openAiClient.completions(textContent).getChoices();
        StringBuilder sb = new StringBuilder();
        for (Choice choice : choices) {
            sb.append(choice.getText());
        }
        return sb.toString();
    }


    // 采用chatgpt3.5-turbo-0301模型
    public String fetchContentByChatGpt3_5(WxMpXmlMessage wxMessage) {
        String textContent = wxMessage.getContent();
        Message message = Message.builder().role(Message.Role.USER).content(textContent).build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(List.of(message)).build();
        ChatCompletionResponse choices = openAiClient.chatCompletion(chatCompletion);

//        Choice[] choices = openAiClient.completions(textContent).getChoices();
        StringBuilder sb = new StringBuilder();
        for (ChatChoice choice : choices.getChoices()) {
            sb.append(choice.getMessage().getContent());
        }
        return sb.toString();
    }

}
