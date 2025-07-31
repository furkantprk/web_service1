// src/test/java/com/example/demo/controller/TestController.java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/test-errors")
public class TestController {

    @GetMapping("/illegal-argument")
    public String throwIllegalArgument() {
        throw new IllegalArgumentException("Test IllegalArgumentException");
    }

    @GetMapping("/authentication")
    public String throwAuthenticationException() throws AuthenticationException {
        throw new AuthenticationException("Test AuthenticationException");
    }

    @GetMapping("/general-exception")
    public String throwGeneralException() {
        throw new RuntimeException("Test General Exception");
    }
}