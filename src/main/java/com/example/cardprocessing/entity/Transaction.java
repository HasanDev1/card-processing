package com.example.cardprocessing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    private UUID externalId;

    private UUID cardId;

    private Double exchangeRate;

    private Long afterBalance;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @Column(length = 3)
    @Enumerated(EnumType.STRING)
    private Currency currency;
}
