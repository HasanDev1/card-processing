package com.example.cardprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateTransactionRequestDto {
    private UUID externalId;
    private Long amount;
    private String currency;
    private String pupose;
}
