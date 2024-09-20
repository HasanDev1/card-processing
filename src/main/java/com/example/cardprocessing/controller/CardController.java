package com.example.cardprocessing.controller;

import com.example.cardprocessing.dto.CreateNewCardRequestDto;
import com.example.cardprocessing.dto.CreateNewCardResponseDto;
import com.example.cardprocessing.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class CardController {

    private final CardService cardService;

    @PostMapping("/cards")
    public ResponseEntity<CreateNewCardResponseDto> createNewCard(@RequestBody @Valid CreateNewCardRequestDto requestDto, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(cardService.createNewCard(requestDto, httpServletRequest));
    }
}
