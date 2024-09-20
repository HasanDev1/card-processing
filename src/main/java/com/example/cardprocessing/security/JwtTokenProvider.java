package com.example.cardprocessing.security;

import com.example.cardprocessing.config.AppProperties;
import com.example.cardprocessing.entity.users.Roles;
import com.example.cardprocessing.entity.users.Users;
import com.example.cardprocessing.exception.ExceptionWithStatusCode;
import com.example.cardprocessing.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final AppProperties appProperties;
    private String secretKey;

//        base64 encodeladik
    @PostConstruct
    void init() {
        secretKey = Base64.getEncoder().encodeToString(appProperties.getKey().getBytes());
    }
    // token yaratamiz
    public String createToken(String username, List<Roles> roles) {

        Date now = new Date();
        Date validate = new Date(now.getTime() + appProperties.getRefreshToken().getExpiry());

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public String resolveToken(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new ExceptionWithStatusCode(HttpStatus.UNAUTHORIZED, 401, "Unauthorized");
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Users getUser(String token) {
        return userRepository.findByUsername(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject()).orElseThrow(() -> new UsernameNotFoundException("this user not found with "+token));
    }
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUser(token).getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
