package com.example.cardprocessing.controller;

import com.example.cardprocessing.dto.CreateNewCardRequestDto;
import com.example.cardprocessing.dto.CreateNewCardResponseDto;
import com.example.cardprocessing.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class CardController {

    private final CardService cardService;

    @PostMapping("/cards")
    public ResponseEntity<CreateNewCardResponseDto> createNewCard(@RequestBody @Valid CreateNewCardRequestDto requestDto, HttpServletRequest httpServletRequest){
        return cardService.createNewCard(requestDto, httpServletRequest);
    }

    @GetMapping("/cards/{cardId}")
    public ResponseEntity<CreateNewCardResponseDto> getCardByCardId(@PathVariable(name = "cardId")UUID cardId){
        return cardService.getCardsByCardId(cardId);
    }


}
