package com.example.demo.expection;

public class RemoteServiceNotFoundException extends RuntimeException {
    public RemoteServiceNotFoundException(String message) {
        super(message);
    }
}
