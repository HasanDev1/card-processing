package com.example.cardprocessing.repository;

import com.example.cardprocessing.entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardsRepository extends JpaRepository<Cards, UUID> {
    Long countByUsersId(Long id);
}
