package com.example.cardprocessing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class TransactionRequest {

    @Id
    private UUID idempotencyId;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
