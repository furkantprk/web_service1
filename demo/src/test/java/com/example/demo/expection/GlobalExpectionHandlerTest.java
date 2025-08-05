package com.example.demo.expection;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleBadRequest() {
        RemoteServiceBadRequestException ex = new RemoteServiceBadRequestException("Bad request");
        ResponseEntity<ErrorResponse> response = handler.handleBadRequest(ex);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Bad request");
    }

    @Test
    void testHandleUnauthorized() {
        RemoteServiceUnauthorizedException ex = new RemoteServiceUnauthorizedException("Unauthorized");
        ResponseEntity<ErrorResponse> response = handler.handleUnauthorized(ex);
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void testHandleNotFound() {
        RemoteServiceNotFoundException ex = new RemoteServiceNotFoundException("Not found");
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void testHandleGeneral() {
        Exception ex = new RuntimeException("Internal error");
        ResponseEntity<ErrorResponse> response = handler.handleGeneral(ex);
        assertThat(response.getStatusCode().value()).isEqualTo(500);
    }
}
