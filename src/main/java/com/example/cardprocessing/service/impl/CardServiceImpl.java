package com.example.cardprocessing.service.impl;

import com.example.cardprocessing.dto.CreateNewCardRequestDto;
import com.example.cardprocessing.dto.CreateNewCardResponseDto;
import com.example.cardprocessing.entity.Cards;
import com.example.cardprocessing.entity.Currency;
import com.example.cardprocessing.entity.users.Status;
import com.example.cardprocessing.entity.users.Users;
import com.example.cardprocessing.exception.ExceptionWithStatusCode;
import com.example.cardprocessing.repository.CardsRepository;
import com.example.cardprocessing.repository.UserRepository;
import com.example.cardprocessing.security.SecurityUtils;
import com.example.cardprocessing.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final CardsRepository cardsRepository;

    @Override
    public CreateNewCardResponseDto createNewCard(CreateNewCardRequestDto requestDto, HttpServletRequest httpServletRequest) {

        if (cardsRepository.countByUsersId(Long.valueOf(requestDto.getUserId()))<4){
            Cards cards = new Cards();
            String id = httpServletRequest.getHeader("Idempotency-Key");
            cards.setIdempotencyKey(UUID.fromString(id));
            cards.setCurrency(Currency.valueOf(requestDto.getCurrency()));
            cards.setUsers(userRepository.findById(Long.parseLong(requestDto.getUserId())));
            cards.setCardId(UUID.randomUUID());
            cards.setStatus(Status.valueOf(requestDto.getStatus()));
            cards.setBalance(Long.valueOf(requestDto.getInitialAmount()));
            cardsRepository.save(cards);
            return new CreateNewCardResponseDto(cards.getCardId(), cards.getUsers().getId(), cards.getStatus().toString(), cards.getBalance(), cards.getCurrency().toString());
        }
        throw new ExceptionWithStatusCode(HttpStatus.BAD_REQUEST, "limit_exceeded" );
    }
}
