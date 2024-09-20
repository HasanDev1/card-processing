package com.example.cardprocessing.service;

import com.example.cardprocessing.dto.CourseFromCbuDto;
import com.example.cardprocessing.dto.CreateTransactionRequestDto;
import com.example.cardprocessing.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction createTransaction(CreateTransactionRequestDto requestDto, UUID cardId, HttpServletRequest httpServletRequest);
    List<CourseFromCbuDto> courseFromCbu();
}
