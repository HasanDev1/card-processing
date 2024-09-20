package com.example.cardprocessing.service.connector;

import com.example.cardprocessing.exception.ExceptionWithStatusCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConnectorMiddleware {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public <T, R> R doRequest(
            String url,
            HttpHeaders headers,
            HttpMethod method,
            T request,
            TypeReference<R> typeResponse) {
        try {
            ResponseEntity<Object> response = restTemplate.exchange(url, method, new HttpEntity<>(request, headers), Object.class);
            log.info("response from middleware: {}", response);
            return objectMapper.convertValue(response.getBody(), typeResponse);
        } catch (HttpStatusCodeException ex) {
            log.error("error response from middleware: HttpCode: {}, body: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ExceptionWithStatusCode(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getStatusCode().value(), ex.getMessage());
        } catch (Exception e) {
            log.error("error response from middleware: {}", e.getMessage());
            throw new ExceptionWithStatusCode(HttpStatus.INTERNAL_SERVER_ERROR, 500, e.getMessage());
        }
    }

}
