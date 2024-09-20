package com.example.cardprocessing.exception;

import org.springframework.http.HttpStatus;

public class ExceptionWithStatusCode extends RuntimeException {
    private HttpStatus httpCode;
    private Integer errorCode;
    private String errorMessage;

    public ExceptionWithStatusCode(HttpStatus httpCode, String errorMessage) {
        super(errorMessage);
        this.httpCode = httpCode;
        this.errorCode = -1;
        this.errorMessage = errorMessage;
    }

    public ExceptionWithStatusCode(HttpStatus httpCode, Integer errorCode, String errorMessage) {
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ExceptionWithStatusCode(Integer errorCode, String errorMessage, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpCode() {
        return httpCode;
    }

    public ExceptionWithStatusCode setHttpCode(HttpStatus httpCode) {
        this.httpCode = httpCode;
        return this;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public ExceptionWithStatusCode setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ExceptionWithStatusCode setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
