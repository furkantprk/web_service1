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
        return new Request.Options(20 * 1000, 60 * 1000); // 20 sn bağlanma, 60 sn yanıtlama

    }
}
