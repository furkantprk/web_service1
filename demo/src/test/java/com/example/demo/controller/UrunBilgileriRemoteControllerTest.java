package com.example.demo.controller;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.expection.RemoteServiceNotFoundException;
import com.example.demo.service.UrunBilgileriRemoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrunBilgileriRemoteController.class)
class UrunBilgileriRemoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrunBilgileriRemoteService service;

    @Autowired
    private ObjectMapper objectMapper;

    private UrunBilgileriDTO sampleDTO;

    @BeforeEach
    void setUp() {
        sampleDTO = new UrunBilgileriDTO();
        sampleDTO.setKrediNumarasi("12345");
        sampleDTO.setSira(1);
        sampleDTO.setRehinDurum(0);
        sampleDTO.setProductLineId(100L);
    }

    @Test
    void getUrunBilgileri_shouldReturnList() throws Exception {
        when(service.getRemoteUrunBilgileri()).thenReturn(List.of(sampleDTO));

        mockMvc.perform(get("/remote/urunler"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].krediNumarasi").value("12345"));
    }

    @Test
    void getUrunlerByKrediNumarasi_shouldReturnList() throws Exception {
        when(service.getRemoteUrunlerByKrediNumarasi("12345")).thenReturn(List.of(sampleDTO));

        mockMvc.perform(get("/remote/urunler/kredi/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sira").value(1));
    }

    @Test
    void getUrunlerByKrediNumarasi_shouldThrowExceptionWhenEmpty() throws Exception {
        when(service.getRemoteUrunlerByKrediNumarasi("empty")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/remote/urunler/kredi/empty"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSiralarByKrediNumarasi_shouldReturnList() throws Exception {
        when(service.getRemoteSiralarByKrediNumarasi("12345")).thenReturn(List.of(1, 2, 3));

        mockMvc.perform(get("/remote/urunler/siralar/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1));
    }

    @Test
    void getSiralarByKrediNumarasi_shouldThrowExceptionWhenEmpty() throws Exception {
        when(service.getRemoteSiralarByKrediNumarasi("empty")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/remote/urunler/siralar/empty"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUrunBilgileri_shouldReturnUpdated() throws Exception {
        when(service.updateRemoteUrunBilgileri(eq("12345"), eq(1), any(UrunBilgileriDTO.class)))
                .thenReturn(sampleDTO);


        mockMvc.perform(put("/remote/urunler/12345/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rehinDurum").value(0));
    }

    @Test
    void updateUrunBilgileri_shouldThrowExceptionWhenNull() throws Exception {
        when(service.updateRemoteUrunBilgileri("12345", 1, sampleDTO)).thenReturn(null);

        mockMvc.perform(put("/remote/urunler/12345/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAndReinsertRemoteEgmStateInformation_shouldReturnOk() throws Exception {
        when(service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira("12345", 1))
                .thenReturn("Başarıyla güncellendi");

        mockMvc.perform(delete("/remote/urunler/delete-and-reinsert-state-info-by-kredi")
                        .param("krediNumarasi", "12345")
                        .param("sira", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Başarıyla güncellendi"));
    }

    @Test
    void deleteAndReinsertRemoteEgmStateInformation_shouldThrowException() throws Exception {
        when(service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira("empty", 1))
                .thenReturn("bilgi bulunamadı");

        mockMvc.perform(delete("/remote/urunler/delete-and-reinsert-state-info-by-kredi")
                        .param("krediNumarasi", "empty")
                        .param("sira", "1"))
                .andExpect(status().isNotFound());
    }
}
