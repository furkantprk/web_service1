package com.example.demo.config;

import com.example.demo.expection.ErrorResponse;
import com.example.demo.expection.RemoteServiceBadRequestException;
import com.example.demo.expection.RemoteServiceNotFoundException;
import com.example.demo.expection.RemoteServiceUnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class FeignErrorDecoderTest {

    private FeignErrorDecoder decoder;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        decoder = new FeignErrorDecoder();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private Response createFakeResponse(int status, String message) throws Exception {
        ErrorResponse errorResponse = new ErrorResponse(status, message, "details", java.time.LocalDateTime.now());
        String body = objectMapper.writeValueAsString(errorResponse);
        return Response.builder()
                .status(status)
                .reason("Error")
                .request(Request.create(Request.HttpMethod.GET, "http://test", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .headers(Collections.emptyMap())
                .body(body, StandardCharsets.UTF_8)
                .build();
    }


    @Test
    public void testInvalidJson() {
        String invalidJson = "INVALID_JSON";
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(Request.HttpMethod.GET, "http://test", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .headers(Collections.emptyMap())
                .body(invalidJson, StandardCharsets.UTF_8)
                .build();

        Exception exception = decoder.decode("methodKey", response);
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception.getMessage().contains("Feign hatası: response parse edilemedi."));
    }
}