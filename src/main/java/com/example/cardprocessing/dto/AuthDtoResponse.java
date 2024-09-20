package com.example.cardprocessing.dto;

import lombok.Data;

@Data
public class AuthDtoResponse {

    private String accessToken;
    private String refreshToken;
    private String username;
    private String tokenType;

}
