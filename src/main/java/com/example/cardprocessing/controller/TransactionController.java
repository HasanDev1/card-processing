package com.example.cardprocessing.controller;

import com.example.cardprocessing.dto.CourseFromCbuDto;
import com.example.cardprocessing.dto.CreateTransactionRequestDto;
import com.example.cardprocessing.dto.TransactionHistoryResponseDto;
import com.example.cardprocessing.entity.Transaction;
import com.example.cardprocessing.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/course")
    public List<CourseFromCbuDto> course(){
        return transactionService.courseFromCbu();
    }

    @PostMapping("/cards/{cardId}/debit")
    public ResponseEntity<Transaction> createTransaction(@PathVariable(name = "cardId")UUID cardId, @RequestBody CreateTransactionRequestDto requestDto, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(transactionService.createTransaction(requestDto, cardId, httpServletRequest));
    }

    @GetMapping("/cards/{cardId}/transactions")
    public ResponseEntity<TransactionHistoryResponseDto> transactionHistory(@PathVariable(name = "cardId") UUID cardId,
                                                                            @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(transactionService.getTransactionHistoryByCardId(cardId, page, size));
    }

}
