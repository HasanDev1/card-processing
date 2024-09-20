package com.example.cardprocessing.repository;

import com.example.cardprocessing.entity.CardRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RequestsRepository extends JpaRepository<CardRequests, UUID> {
    CardRequests findByIdempotencyKey(UUID idempotencyId);
}
