package com.example.cardprocessing.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

@RestControllerAdvice(basePackages = "com.example.cardprocessing")
public class ControllerExceptionHandler {

    @ExceptionHandler({ExceptionWithStatusCode.class})
    public ResponseEntity<ErrorBaseResponse> handledException(ExceptionWithStatusCode e, ServletWebRequest webRequest) {

        if (e.getErrorMessage() == null) {
            e.setErrorMessage("internal.server.error");
        }

        ErrorBaseResponse response = new ErrorBaseResponse(
                e.getErrorCode(),
                e.getErrorMessage()
        );

        return ResponseEntity
                .status(e.getHttpCode())
                .body(response);
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<ErrorBaseResponse> internalError(Throwable e, ServletWebRequest webRequest) {
        e.printStackTrace();
        ErrorBaseResponse response = new ErrorBaseResponse(
                400,
                "something.went.wrong"
        );

        return ResponseEntity
                .status(400)
                .body(response);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorBaseResponse> notValidException(MethodArgumentNotValidException e){
        return ResponseEntity
                .status(400)
                .body(
                        new ErrorBaseResponse(
                                400,
                                e.getFieldErrors().get(0).getDefaultMessage()
                        )
                );
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ErrorBaseResponse> usernameNotFoundException(UsernameNotFoundException e){
        return ResponseEntity
                .status(400)
                .body(
                        new ErrorBaseResponse(
                                400,
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorBaseResponse> notValidException(HttpRequestMethodNotSupportedException e){
        return ResponseEntity
                .status(e.getStatusCode())
                .body(
                        new ErrorBaseResponse(
                                e.getStatusCode().value(),
                                e.getMessage()
                        )
                );
    }
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorBaseResponse> constraintViloationException(ConstraintViolationException e){
        return ResponseEntity
                .status(400)
                .body(
                        new ErrorBaseResponse(
                                400,
                                e.getMessage()
                        ));
    }
}
