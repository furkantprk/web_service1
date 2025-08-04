package com.example.demo.expection;

public class RemoteServiceUnauthorizedException extends RuntimeException {
    public RemoteServiceUnauthorizedException(String message) {
        super(message);
    }
}
