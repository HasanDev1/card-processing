package com.example.cardprocessing.repository;

import com.example.cardprocessing.entity.users.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
//    Optional<RefreshToken> findFirstByRefreshTokenOrderByCreateDateDesc(String refreshToken);

    Optional<RefreshToken> findFirstByRefreshTokenOrderByCreateDateDesc(String refreshToken);

}
