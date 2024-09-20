package com.example.cardprocessing.security;

import com.example.cardprocessing.dto.GetAccessTokenByTokenRequestDto;
import com.example.cardprocessing.entity.users.RefreshToken;
import com.example.cardprocessing.entity.users.Users;
import com.example.cardprocessing.dto.AuthDtoResponse;
import com.example.cardprocessing.dto.LoginRequestDto;
import com.example.cardprocessing.repository.RefreshTokenRepository;
import com.example.cardprocessing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenUtil refreshTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    public AuthDtoResponse createToken(LoginRequestDto request){
        try {
            Users users = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("user not found with "+request.getUsername()));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            String token = jwtTokenProvider.createToken(users.getUsername(),users.getRoles().stream().toList());
            if (token == null || token.isEmpty() || !StringUtils.hasText(token)){
                throw new RuntimeException("Iltimos qayta urining biron nima xato ketdi!");
            }
            //refresh tokenni yaratamiz
            RefreshToken refreshToken = refreshTokenUtil.createRefreshToken(users);
            AuthDtoResponse authPayload = new AuthDtoResponse();
            authPayload.setAccessToken(token);
            authPayload.setRefreshToken(refreshToken.getRefreshToken());
            authPayload.setUsername(users.getUsername());
            authPayload.setTokenType("Bearer");
            return authPayload;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error in Login - {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    public AuthDtoResponse createTokenByUsername(String username) {
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found with "+username));
        if (user == null) {
            throw new RuntimeException("Bu usernameli user mavjud emas!");
        }
        // create token  -  userga vaqtinchalik token biriktiramiz
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles().stream().toList());
        if (token == null || token.isEmpty() || !StringUtils.hasText(token)) {
            throw new RuntimeException("Iltimos qayta urining biron nima xato ketdi!");
        }
//            refresh tokenni yaratamiz
        RefreshToken refreshToken = refreshTokenUtil.createRefreshToken(user);
        AuthDtoResponse authPayload = new AuthDtoResponse();
        authPayload.setAccessToken(token);
        authPayload.setRefreshToken(refreshToken.getRefreshToken());
        authPayload.setUsername(user.getUsername());
        authPayload.setTokenType("Bearer");
        return authPayload;
    }

    public AuthDtoResponse refreshToken(GetAccessTokenByTokenRequestDto getAccessTokenByTokenRequestDto) {
        try {
            RefreshToken refreshToken = refreshTokenRepository.findFirstByRefreshTokenOrderByCreateDateDesc(getAccessTokenByTokenRequestDto.getRefreshToken()).orElseThrow(() -> new RuntimeException("refresh not found"));
            if (!refreshTokenUtil.validateRefreshToken(refreshToken)) {
                throw new RuntimeException("refresh_token is expired");
            }
            return createTokenByUsername(refreshToken.getUser().getUsername());
        } catch (Exception e) {
            log.error("error in Login - {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<?> getMe() {
        String username = securityUtils.getCurrentUser().orElseThrow(()->new RuntimeException("current user not found"));
        return ResponseEntity.ok(userRepository.findByUsername(username));
    }
}
