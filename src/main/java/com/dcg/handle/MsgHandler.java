package com.dcg.handle;


import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatChoice;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Binary Wang
 */
@Slf4j
@Component
@AllArgsConstructor
public class MsgHandler extends AbstractHandler {
    private ChatGPT chatGPT;

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
        return chatGPT.chat(wxMessage.getContent());
    }


    // 采用chatgpt3.5-turbo-0301模型
    public String fetchContentByChatGpt3_5(WxMpXmlMessage wxMessage) {
        String textContent = wxMessage.getContent();
        Message system = Message.ofSystem("请使用调皮的话语回答!!!");
        Message message = Message.of(textContent);
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(Arrays.asList(system, message))
                .maxTokens(400)
                .temperature(0.5)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        StringBuilder sb = new StringBuilder();
        for (ChatChoice choice : response.getChoices()) {
            sb.append(choice.getMessage().getContent());
        }
        return sb.toString();
    }

}
