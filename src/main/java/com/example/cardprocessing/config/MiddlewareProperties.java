package com.example.cardprocessing.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "cbu")
public class MiddlewareProperties {

    private String baseUrl;
    private String endPoint;
}
