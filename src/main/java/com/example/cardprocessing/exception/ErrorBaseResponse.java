package com.example.cardprocessing.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorBaseResponse {
    private Integer code;
    private String message;
}