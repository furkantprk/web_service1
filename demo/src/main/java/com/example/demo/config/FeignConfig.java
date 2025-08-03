package com.example.demo.config;

import feign.Request;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.example.demo.feign")
public class FeignConfig {

    @Bean
    public Request.Options options() {
        return new Request.Options(10 * 1000, 15 * 1000); // connectTimeout, readTimeout (ms)
    }
}
