package com.example.demo.expection;

import com.example.demo.controller.TestController; // Yukarıda oluşturulan test kontrolcüsü
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TestController.class, // Sadece test kontrolcüsünü yükle
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class)) // Exception handler'ı dahil et
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleIllegalArgument_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test-errors/illegal-argument"))
                .andExpect(status().isBadRequest()) // 400 Bad Request
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Test IllegalArgumentException"))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void handleAuthenticationException_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test-errors/authentication"))
                .andExpect(status().isUnauthorized()) // 401 Unauthorized
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Test AuthenticationException"))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void handleGeneralException_ShouldReturnInternalServerError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test-errors/general-exception"))
                .andExpect(status().isInternalServerError()) // 500 Internal Server Error
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Test General Exception"))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void handleNoHandlerFoundException_ShouldReturnNotFound() throws Exception {
        // Mevcut olmayan bir URL'ye istek atarak NoHandlerFoundException tetikle
        mockMvc.perform(MockMvcRequestBuilders.get("/non-existent-url"))
                .andExpect(status().isNotFound()) // 404 Not Found
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists()) // Spring'in varsayılan mesajı
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }
}