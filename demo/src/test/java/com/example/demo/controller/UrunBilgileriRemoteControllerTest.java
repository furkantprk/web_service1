package com.example.demo.controller;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.service.UrunBilgileriRemoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrunBilgileriRemoteController.class) // Sadece bu kontrolcüyü test eder
class UrunBilgileriRemoteControllerTest {

    @Autowired
    private MockMvc mockMvc; // HTTP istekleri simüle etmek için

    @MockBean // Spring kontekstine sahte bir servis ekler
    private UrunBilgileriRemoteService service;

    @Autowired
    private ObjectMapper objectMapper; // JSON serileştirme/deserileştirme için

    private UrunBilgileriDTO urun1;
    private UrunBilgileriDTO urun2;

    @BeforeEach
    void setUp() {
        // Test verilerini hazırla
        urun1 = new UrunBilgileriDTO();
        urun1.setKrediNumarasi("KREDI123");
        urun1.setSira(1);
        urun1.setRehinDurum(0);
        urun1.setProductLineId(101);

        urun2 = new UrunBilgileriDTO();
        urun2.setKrediNumarasi("KREDI456");
        urun2.setSira(2);
        urun2.setRehinDurum(1);
        urun2.setProductLineId(102);
    }

    @Test
    void getRemoteUrunler_ShouldReturnAllUrunler() throws Exception {
        List<UrunBilgileriDTO> urunler = Arrays.asList(urun1, urun2);
        when(service.getRemoteUrunler()).thenReturn(urunler); // Servis metodunu mock'la

        mockMvc.perform(get("/remote/urunler")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200 OK bekleniyor
                .andExpect(jsonPath("$[0].krediNumarasi").value(urun1.getKrediNumarasi()))
                .andExpect(jsonPath("$[1].krediNumarasi").value(urun2.getKrediNumarasi()));
    }

    @Test
    void getRemoteUrunler_ShouldReturnEmptyListWhenNoUrunler() throws Exception {
        when(service.getRemoteUrunler()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/remote/urunler")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Boş liste durumunda da 200 OK beklenir
                .andExpect(jsonPath("$").isEmpty()); // JSON array'in boş olduğunu doğrula
    }

    @Test
    void getRemoteUrunlerByKrediNumarasi_ShouldReturnUrunlerForGivenKrediNumarasi() throws Exception {
        String krediNumarasi = "KREDI123";
        List<UrunBilgileriDTO> urunler = Collections.singletonList(urun1);
        when(service.getRemoteUrunlerByKrediNumarasi(krediNumarasi)).thenReturn(urunler);

        mockMvc.perform(get("/remote/urunler/kredi/{krediNumarasi}", krediNumarasi)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200 OK bekleniyor
                .andExpect(jsonPath("$[0].krediNumarasi").value(krediNumarasi));
    }

    @Test
    void getRemoteUrunlerByKrediNumarasi_ShouldReturnNotFoundWhenNoUrunlerFound() throws Exception {
        String krediNumarasi = "NONEXISTENT";
        when(service.getRemoteUrunlerByKrediNumarasi(krediNumarasi)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/remote/urunler/kredi/{krediNumarasi}", krediNumarasi)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // 404 Not Found bekleniyor
    }

    @Test
    void updateRemoteUrunBilgileri_ShouldUpdateUrunBilgileriSuccessfully() throws Exception {
        String krediNumarasi = "KREDI123";
        Integer sira = 1;
        UrunBilgileriDTO updatedUrun = new UrunBilgileriDTO();
        updatedUrun.setKrediNumarasi(krediNumarasi);
        updatedUrun.setSira(sira);
        updatedUrun.setRehinDurum(1); // Değişen değer
        updatedUrun.setProductLineId(101);

        // Servisin çağrıldığında güncellenmiş DTO'yu döndürmesini bekle
        when(service.updateRemoteUrunBilgileri(anyString(), anyInt(), any(UrunBilgileriDTO.class)))
                .thenReturn(updatedUrun);

        mockMvc.perform(put("/remote/urunler/{krediNumarasi}/{sira}", krediNumarasi, sira)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUrun))) // Request Body'yi JSON'a çevir
                .andExpect(status().isOk()) // 200 OK bekleniyor
                .andExpect(jsonPath("$.rehinDurum").value(1)); // Güncellenen değeri doğrula
    }

    @Test
    void updateRemoteUrunBilgileri_ShouldReturnNotFoundWhenUrunNotFound() throws Exception {
        String krediNumarasi = "NONEXISTENT";
        Integer sira = 99;
        UrunBilgileriDTO updateRequest = new UrunBilgileriDTO();
        updateRequest.setRehinDurum(1);

        when(service.updateRemoteUrunBilgileri(anyString(), anyInt(), any(UrunBilgileriDTO.class)))
                .thenReturn(null); // Servis null döndürdüğünde

        mockMvc.perform(put("/remote/urunler/{krediNumarasi}/{sira}", krediNumarasi, sira)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound()); // 404 Not Found bekleniyor
    }
}