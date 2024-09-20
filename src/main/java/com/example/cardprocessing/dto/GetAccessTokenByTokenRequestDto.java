package com.example.cardprocessing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GetAccessTokenByTokenRequestDto {
    @NotBlank(message = "refreshToken is required")
    private String refreshToken;
}
