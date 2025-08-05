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
import java.time.LocalDateTime;
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
        ErrorResponse errorResponse = new ErrorResponse(status, message, "details", LocalDateTime.now());
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
    public void testBadRequestException() throws Exception {
        // Given
        String expectedMessage = "Bad request error message";
        Response response = createFakeResponse(400, expectedMessage);

        // When
        Exception exception = decoder.decode("testMethod", response);

        // Then
        assertInstanceOf(RemoteServiceBadRequestException.class, exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testUnauthorizedException() throws Exception {
        // Given
        String expectedMessage = "Unauthorized error message";
        Response response = createFakeResponse(401, expectedMessage);

        // When
        Exception exception = decoder.decode("testMethod", response);

        // Then
        assertInstanceOf(RemoteServiceUnauthorizedException.class, exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testNotFoundException() throws Exception {
        // Given
        String expectedMessage = "Not found error message";
        Response response = createFakeResponse(404, expectedMessage);

        // When
        Exception exception = decoder.decode("testMethod", response);

        // Then
        assertInstanceOf(RemoteServiceNotFoundException.class, exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testInternalServerErrorException() throws Exception {
        // Given
        String expectedMessage = "Internal server error message";
        Response response = createFakeResponse(500, expectedMessage);

        // When
        Exception exception = decoder.decode("testMethod", response);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
        assertTrue(exception.getMessage().contains("Uzak sunucu hatası: " + expectedMessage));
    }

    @Test
    public void testOtherStatusCodeException() throws Exception {
        // Given
        String expectedMessage = "Some other error message";
        Response response = createFakeResponse(409, expectedMessage); // Conflict

        // When
        Exception exception = decoder.decode("testMethod", response);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
        assertTrue(exception.getMessage().contains("Uzak sunucu hatası: " + expectedMessage));
    }

    @Test
    public void testInvalidJson() {
        // Given
        String invalidJson = "INVALID_JSON";
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(Request.HttpMethod.GET, "http://test", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .headers(Collections.emptyMap())
                .body(invalidJson, StandardCharsets.UTF_8)
                .build();

        // When
        Exception exception = decoder.decode("methodKey", response);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
        assertTrue(exception.getMessage().contains("Feign hatası: response parse edilemedi."));
        assertNotNull(exception.getCause());
    }

    @Test
    public void testEmptyResponseBody() {
        // Given
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(Request.HttpMethod.GET, "http://test", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .headers(Collections.emptyMap())
                .body("", StandardCharsets.UTF_8)
                .build();

        // When
        Exception exception = decoder.decode("methodKey", response);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
        assertTrue(exception.getMessage().contains("Feign hatası: response parse edilemedi."));
    }

    @Test
    public void testNullResponseBody() {
        // Given
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(Request.HttpMethod.GET, "http://test", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .headers(Collections.emptyMap())
                .body((byte[]) null)
                .build();

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            decoder.decode("methodKey", response);
        });
    }

    @Test
    public void testMethodKeyParameter() throws Exception {
        // Given
        String methodKey = "UserService#getUser(Long)";
        String expectedMessage = "User not found";
        Response response = createFakeResponse(404, expectedMessage);

        // When
        Exception exception = decoder.decode(methodKey, response);

        // Then
        assertInstanceOf(RemoteServiceNotFoundException.class, exception);
        assertEquals(expectedMessage, exception.getMessage());
        // Method key is passed to decode method but not used in current implementation
        // This test ensures the method signature works correctly
    }
}