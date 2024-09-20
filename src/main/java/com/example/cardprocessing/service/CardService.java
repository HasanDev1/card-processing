package com.example.cardprocessing.service;

import com.example.cardprocessing.dto.CreateNewCardRequestDto;
import com.example.cardprocessing.dto.CreateNewCardResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface CardService {

    CreateNewCardResponseDto createNewCard(CreateNewCardRequestDto requestDto, HttpServletRequest httpServletRequest);
}
