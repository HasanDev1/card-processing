package com.example.cardprocessing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CardProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardProcessingApplication.class, args);
        log.info("documentation for: http://localhost:8080/swagger-ui/index.html#/");
    }

}
