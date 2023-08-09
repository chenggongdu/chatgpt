package com.dcg.controller;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatChoice;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

/**
 * 测试openai接口
 */
@Controller
@AllArgsConstructor
public class OpenaiTestController {

    private final ChatGPT chatGPT;


    @GetMapping("/test/chat")
    @ResponseBody
    public String chat(@RequestParam("msg") String msg) {
        Message system = Message.ofSystem("请使用调皮的话语回答!!!");
        Message message = Message.of(msg);
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(Arrays.asList(system, message))
                .maxTokens(500)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        StringBuilder sb = new StringBuilder();
        for (ChatChoice choice : response.getChoices()) {
            sb.append(choice.getMessage().getContent());
        }
        return sb.toString();
    }


    @GetMapping("/test/index")
    @ResponseBody
    public String index(@RequestParam("msg") String msg) {
        return "success";
    }
}