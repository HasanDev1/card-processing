package com.example.cardprocessing.security;

import com.example.cardprocessing.config.JwtProperties;
import com.example.cardprocessing.entity.users.RefreshToken;
import com.example.cardprocessing.entity.users.Users;
import com.example.cardprocessing.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenUtil {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties appProperties;

    public RefreshToken createRefreshToken(Users users){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(users);
        refreshToken.setExpiredTime(new Date(new Date().getTime()+appProperties.getRefreshToken().getExpiry()));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public boolean validateRefreshToken(RefreshToken refreshToken){
        return refreshToken.getExpiredTime().after(new Date());
    }

}
