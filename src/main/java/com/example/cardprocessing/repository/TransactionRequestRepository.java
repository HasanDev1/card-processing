package com.example.cardprocessing.repository;

import com.example.cardprocessing.entity.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, UUID> {

    TransactionRequest findByIdempotencyId(UUID idempotencyId);
}
