package com.example.cardprocessing.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "secret")
public class JwtProperties {

    private String key;
    private RefreshToken refreshToken;

    @Data
    public static class RefreshToken{
        private Long expiry;
    }
}
