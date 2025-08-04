package com.example.demo.expection;

public class RemoteServiceBadRequestException extends RuntimeException {
    public RemoteServiceBadRequestException(String message) {
        super(message);
    }
}
