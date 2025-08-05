package com.example.demo.config;

import com.example.demo.expection.ErrorResponse;
import com.example.demo.expection.RemoteServiceBadRequestException;
import com.example.demo.expection.RemoteServiceUnauthorizedException;
import com.example.demo.expection.RemoteServiceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    public FeignErrorDecoder() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream bodyStream = response.body().asInputStream()) {
            ErrorResponse errorResponse = objectMapper.readValue(bodyStream, ErrorResponse.class);
            HttpStatus status = HttpStatus.valueOf(response.status());

            return switch (status) {
                case BAD_REQUEST -> new RemoteServiceBadRequestException(errorResponse.getMessage());
                case UNAUTHORIZED -> new RemoteServiceUnauthorizedException(errorResponse.getMessage());
                case NOT_FOUND -> new RemoteServiceNotFoundException(errorResponse.getMessage());
                default -> new RuntimeException("Uzak sunucu hatası: " + errorResponse.getMessage());
            };
        } catch (IOException e) {
            return new RuntimeException("Feign hatası: response parse edilemedi.", e);
        }
    }
}
