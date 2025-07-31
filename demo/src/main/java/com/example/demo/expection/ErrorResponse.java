package com.example.demo.expection;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private String details;
    private LocalDateTime timestamp;
}