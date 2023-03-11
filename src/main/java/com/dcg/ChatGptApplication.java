package com.dcg;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;

@EnableScheduling
@SpringBootApplication
public class ChatGptApplication {

    @Value("${openai.apiKey}")
    private String openaiApiKey;

    public static void main(String[] args) {
        SpringApplication.run(ChatGptApplication.class);
    }


    @Bean
    public OpenAiClient openAiClient() {
        // 若服务器可访问外网，可不使用代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 21882));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return OpenAiClient.builder()
                .apiKey(openaiApiKey)
                .connectTimeout(50)
                .writeTimeout(50)
                .readTimeout(50)
                .interceptor(Collections.singletonList(httpLoggingInterceptor))
                .proxy(proxy)
                .build();
    }
}
