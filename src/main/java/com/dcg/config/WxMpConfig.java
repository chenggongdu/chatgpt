package com.dcg.config;

import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfig {

    private WxMpProperties properties;

    @Bean
    public WxMpService wxMpService() {
        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setToken(properties.getToken());
        configStorage.setAppId(properties.getAppId());
        configStorage.setAesKey(properties.getAesKey());
        configStorage.setSecret(properties.getSecret());
        service.setWxMpConfigStorage(configStorage);
        return service;
    }
}
