package com.example.cardprocessing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateNewCardRequestDto {

    @NotBlank(message = "user_id is required")
    @Pattern(regexp = "^\\d+$", message = "user_id must be only digits")
    @JsonProperty("user_id")
    private String userId;

    private String status;

    @JsonProperty("initial_amount")
    private String initialAmount;

    private String currency;
}
