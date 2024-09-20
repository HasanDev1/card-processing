package com.example.cardprocessing.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum Currency {
    UZS("000"), USD("840");

    private final String courseType;
}
