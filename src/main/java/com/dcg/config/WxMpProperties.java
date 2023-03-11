package com.dcg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wx.mp")
public class WxMpProperties {

    /**
     * 设置微信公众号的appid
     */
    private String appId;

    /**
     * 设置微信公众号的app secret
     */
    private String secret;

    private String token;

    private String aesKey;
}
