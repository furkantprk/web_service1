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
import static org.mockito.ArgumentMatchers.eq; // 'eq' için import eklendi
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

    private UrunBilgileriDTO urun1;
    private UrunBilgileriDTO urun2;

    @BeforeEach
    void setUp() {
        urun1 = new UrunBilgileriDTO();
        urun1.setKrediNumarasi("KREDI123");
        urun1.setSira(1);
        urun1.setRehinDurum(0);
        urun1.setProductLineId(101L);

        urun2 = new UrunBilgileriDTO();
        urun2.setKrediNumarasi("KREDI456");
        urun2.setSira(2);
        urun2.setRehinDurum(1);
        urun2.setProductLineId(102L);
    }

    @Test
    void getRemoteUrunBilgileri_ShouldReturnAllUrunler() throws Exception {
        List<UrunBilgileriDTO> urunler = Arrays.asList(urun1, urun2);
        when(service.getRemoteUrunBilgileri()).thenReturn(urunler);

        mockMvc.perform(get("/remote/urunler")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].krediNumarasi").value(urun1.getKrediNumarasi()))
                .andExpect(jsonPath("$[1].krediNumarasi").value(urun2.getKrediNumarasi()));
    }

    @Test
    void getRemoteUrunBilgileri_ShouldReturnEmptyListWhenNoUrunler() throws Exception {
        when(service.getRemoteUrunBilgileri()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/remote/urunler")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getRemoteUrunlerByKrediNumarasi_ShouldReturnUrunlerForGivenKrediNumarasi() throws Exception {
        String krediNumarasi = "KREDI123";
        List<UrunBilgileriDTO> urunler = Collections.singletonList(urun1);
        when(service.getRemoteUrunlerByKrediNumarasi(krediNumarasi)).thenReturn(urunler);

        mockMvc.perform(get("/remote/urunler/kredi/{krediNumarasi}", krediNumarasi)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].krediNumarasi").value(krediNumarasi));
    }

    @Test
    void getRemoteUrunlerByKrediNumarasi_ShouldReturnNotFoundWhenNoUrunlerFound() throws Exception {
        String krediNumarasi = "NONEXISTENT";
        when(service.getRemoteUrunlerByKrediNumarasi(krediNumarasi)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/remote/urunler/kredi/{krediNumarasi}", krediNumarasi)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSiralarByKrediNumarasi_ShouldReturnListOfSiralar() throws Exception {
        String krediNumarasi = "KREDI123";
        List<Integer> siralar = Arrays.asList(1, 3, 5);
        when(service.getRemoteSiralarByKrediNumarasi(krediNumarasi)).thenReturn(siralar);

        mockMvc.perform(get("/remote/urunler/siralar/{krediNumarasi}", krediNumarasi)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(3))
                .andExpect(jsonPath("$[2]").value(5));
    }

    @Test
    void getSiralarByKrediNumarasi_ShouldReturnNotFoundWhenNoSiralarFound() throws Exception {
        String krediNumarasi = "NONEXISTENT";
        when(service.getRemoteSiralarByKrediNumarasi(krediNumarasi)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/remote/urunler/siralar/{krediNumarasi}", krediNumarasi)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRemoteUrunBilgileri_ShouldUpdateUrunBilgileriSuccessfully() throws Exception {
        String krediNumarasi = "KREDI123";
        Integer sira = 1;
        UrunBilgileriDTO updatedUrun = new UrunBilgileriDTO();
        updatedUrun.setKrediNumarasi(krediNumarasi);
        updatedUrun.setSira(sira);
        updatedUrun.setRehinDurum(1);
        updatedUrun.setProductLineId(101L);

        when(service.updateRemoteUrunBilgileri(anyString(), anyInt(), any(UrunBilgileriDTO.class)))
                .thenReturn(updatedUrun);

        mockMvc.perform(put("/remote/urunler/{krediNumarasi}/{sira}", krediNumarasi, sira)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUrun)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rehinDurum").value(1));
    }

    @Test
    void updateRemoteUrunBilgileri_ShouldReturnNotFoundWhenUrunNotFound() throws Exception {
        String krediNumarasi = "NONEXISTENT";
        Integer sira = 99;
        UrunBilgileriDTO updateRequest = new UrunBilgileriDTO();
        updateRequest.setRehinDurum(1);
        updateRequest.setProductLineId(999L);

        when(service.updateRemoteUrunBilgileri(anyString(), anyInt(), any(UrunBilgileriDTO.class)))
                .thenReturn(null);

        mockMvc.perform(put("/remote/urunler/{krediNumarasi}/{sira}", krediNumarasi, sira)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAndReinsertRemoteEgmStateInformationByKrediNumarasi_ShouldReturnOkForSpecificSira() throws Exception {
        String krediNumarasi = "KREDI123";
        Integer sira = 1;
        String expectedMessage = "Kredi numarası: KREDI123 ve sıra: 1 için EgmStateInformation başarıyla güncellendi.";
        // Servis metodunun doğru parametrelerle çağrıldığını mock'la
        when(service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(eq(krediNumarasi), eq(sira)))
                .thenReturn(expectedMessage);

        mockMvc.perform(delete("/remote/urunler/delete-and-reinsert-state-info-by-kredi")
                        .param("krediNumarasi", krediNumarasi) // Query parametre olarak gönder
                        .param("sira", String.valueOf(sira)) // Query parametre olarak gönder
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    void deleteAndReinsertRemoteEgmStateInformationByKrediNumarasi_ShouldReturnOkForAllSiralar() throws Exception {
        String krediNumarasi = "KREDI123";
        // 'sira' parametresi gönderilmeyecek, bu da 'null' anlamına gelecek
        String expectedMessage = "Kredi numarası: KREDI123 için EgmStateInformation başarıyla güncellendi.";
        when(service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(eq(krediNumarasi), eq(null))) // null bekliyoruz
                .thenReturn(expectedMessage);

        mockMvc.perform(delete("/remote/urunler/delete-and-reinsert-state-info-by-kredi")
                        .param("krediNumarasi", krediNumarasi) // Sadece krediNumarasi gönder
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    void deleteAndReinsertRemoteEgmStateInformationByKrediNumarasi_ShouldReturnNotFound() throws Exception {
        String krediNumarasi = "NONEXISTENT";
        Integer sira = 1;
        String errorMessage = "Kredi numarası: NONEXISTENT ve sıra: 1 ile eşleşen kayıt bulunamadı.";
        // Servis metodunun belirli bir hata mesajı döndürmesini mock'la
        when(service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(eq(krediNumarasi), eq(sira)))
                .thenReturn(errorMessage);

        mockMvc.perform(delete("/remote/urunler/delete-and-reinsert-state-info-by-kredi")
                        .param("krediNumarasi", krediNumarasi)
                        .param("sira", String.valueOf(sira))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Servis mesajına göre 404 dönüyoruz
                .andExpect(content().string(errorMessage));
    }

    @Test
    void deleteAndReinsertRemoteEgmStateInformationByKrediNumarasi_ShouldReturnInternalServerError() throws Exception {
        String krediNumarasi = "ERROR_CASE";
        Integer sira = 1;
        // Servis metodunun hata fırlatmasını mock'la
        when(service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(eq(krediNumarasi), eq(sira)))
                .thenThrow(new RuntimeException("Uzak servis bilinmeyen bir hata verdi."));

        mockMvc.perform(delete("/remote/urunler/delete-and-reinsert-state-info-by-kredi")
                        .param("krediNumarasi", krediNumarasi)
                        .param("sira", String.valueOf(sira))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Uzak serviste işlem sırasında bir hata oluştu: Uzak servis bilinmeyen bir hata verdi."));
    }
}