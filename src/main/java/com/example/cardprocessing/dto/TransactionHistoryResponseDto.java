package com.example.cardprocessing.dto;

import com.example.cardprocessing.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionHistoryResponseDto {
    private Long page;
    private Long size;
    private Long totalPages;
    private Long totalItems;
    private List<Transaction> content;
}
