package com.dcg;

import com.plexpt.chatgpt.ChatGPT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetSocketAddress;
import java.net.Proxy;

@EnableScheduling
@SpringBootApplication
public class ChatGptApplication {

    @Value("${openai.apiKey}")
    private String openaiApiKey;
    @Value("${openai.apiHost}")
    private String openaiApiHost;

    public static void main(String[] args) {
        SpringApplication.run(ChatGptApplication.class);
    }


    @Bean
    public ChatGPT chatGPT() {
        // 若服务器可访问外网，可不使用代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));

        return ChatGPT.builder()
                .apiKey(openaiApiKey)
//                .proxy(proxy)
                .apiHost(openaiApiHost) //反向代理地址
                .build()
                .init();
    }
}
