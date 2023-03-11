package com.dcg.controller;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.common.Choice;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试openai接口
 */
@Controller
@AllArgsConstructor
public class OpenaiTestController {

    private final OpenAiClient openAiClient;


    @GetMapping("/test/chat")
    @ResponseBody
    public String chat(@RequestParam("msg") String msg) {
        CompletionResponse completions = openAiClient.completions(msg);
        StringBuilder sb = new StringBuilder();
        for (Choice choice : completions.getChoices()) {
            sb.append(choice.getText());
        }
        return sb.toString();
    }


    @GetMapping("/test/index")
    @ResponseBody
    public String index(@RequestParam("msg") String msg) {
        return "success";
    }
}