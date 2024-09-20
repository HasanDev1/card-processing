package com.example.cardprocessing.controller;

import com.example.cardprocessing.dto.GetAccessTokenByTokenRequestDto;
import com.example.cardprocessing.dto.LoginRequestDto;
import com.example.cardprocessing.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto request){
        return ResponseEntity.ok(authService.createToken(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid GetAccessTokenByTokenRequestDto getAccessTokenByTokenRequestDto) {
        return ResponseEntity.ok(authService.refreshToken(getAccessTokenByTokenRequestDto));
    }

    @GetMapping("/me")
    public ResponseEntity<?>getMe(){
        return authService.getMe();
    }
}
