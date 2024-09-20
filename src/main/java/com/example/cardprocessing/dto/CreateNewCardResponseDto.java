package com.example.cardprocessing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateNewCardResponseDto {

    @JsonProperty("card_id")
    private UUID cardId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("status")
    private String status;

    private Long balance;
    private String currency;
}
