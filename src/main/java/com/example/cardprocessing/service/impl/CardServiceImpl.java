package com.example.cardprocessing.service.impl;

import com.example.cardprocessing.dto.CreateNewCardRequestDto;
import com.example.cardprocessing.dto.CreateNewCardResponseDto;
import com.example.cardprocessing.entity.CardRequests;
import com.example.cardprocessing.entity.Cards;
import com.example.cardprocessing.entity.Currency;
import com.example.cardprocessing.entity.users.Status;
import com.example.cardprocessing.exception.ExceptionWithStatusCode;
import com.example.cardprocessing.repository.CardsRepository;
import com.example.cardprocessing.repository.RequestsRepository;
import com.example.cardprocessing.repository.UserRepository;
import com.example.cardprocessing.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final RequestsRepository requestsRepository;
    private final UserRepository userRepository;
    private final CardsRepository cardsRepository;

    @Override
    public ResponseEntity<CreateNewCardResponseDto> createNewCard(CreateNewCardRequestDto requestDto, HttpServletRequest httpServletRequest) {
        String idempotencyId = httpServletRequest.getHeader("Idempotency-Key");
        CardRequests requests = getRequest(UUID.fromString(idempotencyId));
        if (Objects.isNull(requests)) {
            if (cardsRepository.countByUsersId(Long.valueOf(requestDto.getUserId())) < 4) {
                Cards cards = new Cards();
                cards.setEtag(UUID.randomUUID());
                cards.setCurrency(Currency.valueOf(requestDto.getCurrency()));
                cards.setUsers(userRepository.findById(Long.parseLong(requestDto.getUserId())));
                cards.setCardId(UUID.randomUUID());
                cards.setStatus(Status.valueOf(requestDto.getStatus()));
                cards.setBalance(Long.valueOf(requestDto.getInitialAmount()));
                cardsRepository.save(cards);
                saveCardRequest(UUID.fromString(idempotencyId), cards);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("ETag", cards.getEtag().toString());
                return new ResponseEntity<>(new CreateNewCardResponseDto(cards.getCardId(), cards.getUsers().getId(), cards.getStatus().toString(), cards.getBalance(), cards.getCurrency().toString()), httpHeaders, HttpStatus.CREATED);
            }
            throw new ExceptionWithStatusCode(HttpStatus.BAD_REQUEST, "limit_exceeded");
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ETag", requests.getCards().getEtag().toString());
        return new ResponseEntity<>(new CreateNewCardResponseDto(requests.getCards().getCardId(), requests.getCards().getUsers().getId(), requests.getCards().getStatus().toString(), requests.getCards().getBalance(), requests.getCards().getCurrency().toString()), httpHeaders, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CreateNewCardResponseDto> getCardsByCardId(UUID cardId){
        Cards cards = cardsRepository.findByCardId(cardId).orElseThrow(() -> new ExceptionWithStatusCode(HttpStatus.NOT_FOUND, 404, "Card with such id not exists in processing." ));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ETag", cards.getEtag().toString());
        return new ResponseEntity<>(new CreateNewCardResponseDto(cards.getCardId(), cards.getUsers().getId(), cards.getStatus().toString(), cards.getBalance(), cards.getCurrency().toString()), httpHeaders, HttpStatus.OK);
    }

    @Override
    public void blockCard(UUID cardId) {
        Cards cards = cardsRepository.findByCardId(cardId).orElseThrow(() -> new ExceptionWithStatusCode(HttpStatus.NOT_FOUND, 404, "Card with such id not exists in processing." ));
        if (cards.getStatus().equals(Status.ACTIVE)){
            cards.setStatus(Status.BLOCKED);
            cardsRepository.save(cards);
        }else throw new ExceptionWithStatusCode(HttpStatus.BAD_REQUEST, 500, "card already blocked or closed");
    }

    @Override
    public void unblockCard(UUID cardId) {
        Cards cards = cardsRepository.findByCardId(cardId).orElseThrow(() -> new ExceptionWithStatusCode(HttpStatus.NOT_FOUND, 404, "Card with such id not exists in processing." ));
        if (cards.getStatus().equals(Status.BLOCKED)){
            cards.setStatus(Status.ACTIVE);
            cardsRepository.save(cards);
        }else throw new ExceptionWithStatusCode(HttpStatus.BAD_REQUEST, 500, "card already activated or closed");
    }

    @Override
    public CardRequests saveCardRequest(UUID idempotencyId, Cards cards) {
        return requestsRepository.save(new CardRequests(idempotencyId, cards));
    }

    @Override
    public CardRequests getRequest(UUID idempotencyId) {
        return requestsRepository.findByIdempotencyKey(idempotencyId);
    }



}
