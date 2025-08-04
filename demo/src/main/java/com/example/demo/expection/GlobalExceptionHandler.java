package com.example.demo.expection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RemoteServiceBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(RemoteServiceBadRequestException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "Remote service returned BAD_REQUEST",
                LocalDateTime.now()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RemoteServiceUnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(RemoteServiceUnauthorizedException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                "Remote service returned UNAUTHORIZED",
                LocalDateTime.now()
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RemoteServiceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RemoteServiceNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                "Remote service returned NOT_FOUND",
                LocalDateTime.now()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                "Bilinmeyen bir hata oluştu",
                LocalDateTime.now()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
