package com.example.cardprocessing.service.impl;

import com.example.cardprocessing.config.MiddlewareProperties;
import com.example.cardprocessing.dto.CourseFromCbuDto;
import com.example.cardprocessing.dto.CreateTransactionRequestDto;
import com.example.cardprocessing.dto.TransactionHistoryResponseDto;
import com.example.cardprocessing.entity.*;
import com.example.cardprocessing.entity.users.Status;
import com.example.cardprocessing.exception.ExceptionWithStatusCode;
import com.example.cardprocessing.repository.CardsRepository;
import com.example.cardprocessing.repository.TransactionRepository;
import com.example.cardprocessing.repository.TransactionRequestRepository;
import com.example.cardprocessing.service.TransactionService;
import com.example.cardprocessing.service.connector.ConnectorMiddleware;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRequestRepository transactionRequestRepository;
    private final TransactionRepository transactionRepository;
    private final CardsRepository cardsRepository;
    private final HttpHeaders httpHeaders;
    private final MiddlewareProperties middlewareProperties;
    private final ConnectorMiddleware connectorMiddleware;

    public TransactionServiceImpl(TransactionRequestRepository transactionRequestRepository, TransactionRepository transactionRepository, CardsRepository cardsRepository, MiddlewareProperties middlewareProperties, ConnectorMiddleware connectorMiddleware) {
        this.transactionRequestRepository = transactionRequestRepository;
        this.transactionRepository = transactionRepository;
        this.cardsRepository = cardsRepository;
        this.middlewareProperties = middlewareProperties;
        this.connectorMiddleware = connectorMiddleware;

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public Transaction createTransaction(CreateTransactionRequestDto requestDto, UUID cardId, HttpServletRequest httpServletRequest) {
        String idempotencyId = httpServletRequest.getHeader("Idempotency-Key");
        Cards card = cardsRepository.findByCardId(cardId).orElseThrow(() -> new ExceptionWithStatusCode(HttpStatus.NOT_FOUND, 404, "card not found this card %s".formatted(cardId)));
        TransactionRequest requests = getTransactionRequest(UUID.fromString(idempotencyId));
        if (Objects.isNull(requests)) {
            if (!Objects.equals(card.getStatus(), Status.ACTIVE)){
                throw new ExceptionWithStatusCode(HttpStatus.BAD_REQUEST, 400, "card is not ACTIVE");
            }
            if (Objects.equals(Currency.valueOf(requestDto.getCurrency()), card.getCurrency())) {
                Transaction transaction = new Transaction();
                transaction.setCardId(cardId);
                transaction.setAmount(requestDto.getAmount());
                transaction.setExternalId(requestDto.getExternalId());
                transaction.setCurrency(Currency.valueOf(requestDto.getCurrency()));
                transaction.setPurpose(Purpose.valueOf(requestDto.getPupose()));
                transaction.setExchangeRate(null);
                card.setBalance(card.getBalance() - requestDto.getAmount());
                cardsRepository.save(card);
                transaction.setAfterBalance(card.getBalance());
                transaction = transactionRepository.save(transaction);
                saveTransactionRequest(UUID.fromString(idempotencyId), transaction);
                return transaction;
            } else {
                CourseFromCbuDto course = getCourseType(card.getCurrency().getCourseType());
                Transaction transaction = new Transaction();
                transaction.setCardId(cardId);
                transaction.setAmount(requestDto.getAmount());
                transaction.setExternalId(requestDto.getExternalId());
                transaction.setCurrency(Currency.valueOf(requestDto.getCurrency()));
                transaction.setPurpose(Purpose.valueOf(requestDto.getPupose()));
                transaction.setExchangeRate(Double.valueOf(course.getRate()));
                card.setBalance(card.getBalance() - requestDto.getAmount() / (long) Double.parseDouble(course.getRate()));
                cardsRepository.save(card);
                transaction.setAfterBalance(card.getBalance());
                transaction = transactionRepository.save(transaction);
                saveTransactionRequest(UUID.fromString(idempotencyId), transaction);
                return transaction;
            }
        }
        return transactionRequestRepository.findByIdempotencyId(UUID.fromString(idempotencyId)).getTransaction();
    }

    @Override
    public TransactionHistoryResponseDto getTransactionHistoryByCardId(UUID cardId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Transaction> transactionList = transactionRepository.findAllByCardId(cardId, pageable);
        TransactionHistoryResponseDto responseDto = new TransactionHistoryResponseDto();
        responseDto.setSize(Long.valueOf(transactionList.getSize()));
        responseDto.setPage(Long.valueOf(transactionList.getPageable().getPageNumber()));
        responseDto.setTotalItems(transactionList.getTotalElements());
        responseDto.setTotalPages(Long.valueOf(transactionList.getTotalPages()));
        responseDto.setContent(transactionList.getContent());
        return responseDto;
    }

    TransactionRequest getTransactionRequest(UUID idempotencyId) {
        return transactionRequestRepository.findByIdempotencyId(idempotencyId);
    }

    void saveTransactionRequest(UUID idempotencyId, Transaction transaction) {
        transactionRequestRepository.save(new TransactionRequest(idempotencyId, transaction));
    }

    public List<CourseFromCbuDto> courseFromCbu() {
        return connectorMiddleware.doRequest(
                middlewareProperties.getBaseUrl()
                        .concat(middlewareProperties.getEndPoint())
                        .concat("/" + LocalDate.now()), httpHeaders, HttpMethod.GET, null, new TypeReference<>() {
                });
    }

    private CourseFromCbuDto getCourseType(String code) {
        List<CourseFromCbuDto> course = courseFromCbu();
        for (CourseFromCbuDto item : course) {
            if (Objects.equals(item.getCode(), code)) {
                return item;
            }
        }
        throw new ExceptionWithStatusCode(HttpStatus.INTERNAL_SERVER_ERROR, 500, "course not found with %s".formatted(code));
    }

}

