package com.example.cardprocessing.service;

import com.example.cardprocessing.dto.CreateNewCardRequestDto;
import com.example.cardprocessing.dto.CreateNewCardResponseDto;
import com.example.cardprocessing.entity.Cards;
import com.example.cardprocessing.entity.CardRequests;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CardService {
    ResponseEntity<CreateNewCardResponseDto> createNewCard(CreateNewCardRequestDto requestDto, HttpServletRequest httpServletRequest);

    ResponseEntity<CreateNewCardResponseDto> getCardsByCardId(UUID cardId);

    void blockCard(UUID cardId);
    void unblockCard(UUID cardId);

    CardRequests saveCardRequest(UUID idempotencyId, Cards cards);
    CardRequests getRequest(UUID idempotencyId);
}
