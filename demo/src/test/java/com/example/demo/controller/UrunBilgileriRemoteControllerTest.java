package com.example.demo.controller;

import com.example.demo.dto.KO_OtoEvrakDurumDTO;
import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.expection.RemoteServiceNotFoundException;
import com.example.demo.service.UrunBilgileriRemoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
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

    // Test verileri oluşturmak için yardımcı metotlar ve sabitler
    private final String KREDI_NUMARASI = "12345";
    private final String EVRAK_KODU = "EGM_STATE_INFO";
    private final Integer SIRA = 1;
    private final Long PRODUCT_LINE_ID = 100L;
    private final Integer REHIN_DURUM = 0;
    private final Integer DURUM = 1;

    private UrunBilgileriDTO createUrunBilgileriDTO(String krediNumarasi, Integer sira) {
        UrunBilgileriDTO dto = new UrunBilgileriDTO();
        dto.setKrediNumarasi(krediNumarasi);
        dto.setSira(sira);
        dto.setRehinDurum(REHIN_DURUM);
        dto.setProductLineId(PRODUCT_LINE_ID);
        return dto;
    }

    private KO_OtoEvrakDurumDTO createKoOtoEvrakDurumDTO(String krediNumarasi, String evrakKodu) {
        KO_OtoEvrakDurumDTO dto = new KO_OtoEvrakDurumDTO();
        dto.setKrediNumarasi(krediNumarasi);
        dto.setEvrakKodu(evrakKodu);
        dto.setDurum(DURUM);
        return dto;
    }

    private UrunBilgileriDTO sampleUrunDTO;

    @BeforeEach
    void setUp() {
        sampleUrunDTO = new UrunBilgileriDTO();
        sampleUrunDTO.setKrediNumarasi(KREDI_NUMARASI);
        sampleUrunDTO.setSira(SIRA);
        sampleUrunDTO.setRehinDurum(REHIN_DURUM);
        sampleUrunDTO.setProductLineId(PRODUCT_LINE_ID);
    }

    // GET /remote/urunler
    @Test
    void getUrunBilgileri_shouldReturnListOfUrunBilgileriDTO() throws Exception {
        // Arrange
        List<UrunBilgileriDTO> urunler = List.of(
                createUrunBilgileriDTO(KREDI_NUMARASI, SIRA),
                createUrunBilgileriDTO(KREDI_NUMARASI, SIRA + 1)
        );
        when(service.getRemoteUrunBilgileri()).thenReturn(urunler);

        // Act & Assert
        mockMvc.perform(get("/remote/urunler"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].krediNumarasi").value(KREDI_NUMARASI));

        verify(service, times(1)).getRemoteUrunBilgileri();
    }

    // GET /remote/urunler/kredi/{krediNumarasi}
    @Test
    void getUrunlerByKrediNumarasi_shouldReturnListOfUrunBilgileriDTO() throws Exception {
        // Arrange
        List<UrunBilgileriDTO> urunler = List.of(createUrunBilgileriDTO(KREDI_NUMARASI, SIRA));
        when(service.getRemoteUrunlerByKrediNumarasi(KREDI_NUMARASI)).thenReturn(urunler);

        // Act & Assert
        mockMvc.perform(get("/remote/urunler/kredi/{krediNumarasi}", KREDI_NUMARASI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].krediNumarasi").value(KREDI_NUMARASI));

        verify(service, times(1)).getRemoteUrunlerByKrediNumarasi(KREDI_NUMARASI);
    }

    @Test
    void getUrunlerByKrediNumarasi_shouldThrowNotFoundException() throws Exception {
        // Arrange
        when(service.getRemoteUrunlerByKrediNumarasi(KREDI_NUMARASI)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/remote/urunler/kredi/{krediNumarasi}", KREDI_NUMARASI))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Girilen kredi numarasına ait ürün bilgisi bulunamadı."));

        verify(service, times(1)).getRemoteUrunlerByKrediNumarasi(KREDI_NUMARASI);
    }

    // GET /remote/urunler/siralar/{krediNumarasi}
    @Test
    void getSiralarByKrediNumarasi_shouldReturnListOfSiras() throws Exception {
        // Arrange
        List<Integer> siralar = List.of(SIRA, SIRA + 1);
        when(service.getRemoteSiralarByKrediNumarasi(KREDI_NUMARASI)).thenReturn(siralar);

        // Act & Assert
        mockMvc.perform(get("/remote/urunler/siralar/{krediNumarasi}", KREDI_NUMARASI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0]").value(SIRA));

        verify(service, times(1)).getRemoteSiralarByKrediNumarasi(KREDI_NUMARASI);
    }

    @Test
    void getSiralarByKrediNumarasi_shouldThrowNotFoundException() throws Exception {
        // Arrange
        when(service.getRemoteSiralarByKrediNumarasi(KREDI_NUMARASI)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/remote/urunler/siralar/{krediNumarasi}", KREDI_NUMARASI))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Girilen kredi numarasına ait sıra bilgisi bulunamadı."));

        verify(service, times(1)).getRemoteSiralarByKrediNumarasi(KREDI_NUMARASI);
    }

    // PUT /remote/urunler/{krediNumarasi}/{sira}
    @Test
    void updateUrunBilgileri_shouldReturnUpdatedDTO() throws Exception {
        // Arrange
        UrunBilgileriDTO updatedDTO = createUrunBilgileriDTO(KREDI_NUMARASI, SIRA);
        updatedDTO.setRehinDurum(1);

        when(service.updateRemoteUrunBilgileri(eq(KREDI_NUMARASI), eq(SIRA), any(UrunBilgileriDTO.class)))
                .thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/remote/urunler/{krediNumarasi}/{sira}", KREDI_NUMARASI, SIRA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rehinDurum").value(1));

        verify(service, times(1)).updateRemoteUrunBilgileri(eq(KREDI_NUMARASI), eq(SIRA), any(UrunBilgileriDTO.class));
    }

    @Test
    void updateUrunBilgileri_shouldThrowNotFoundException() throws Exception {
        // Arrange
        UrunBilgileriDTO updateData = createUrunBilgileriDTO(KREDI_NUMARASI, SIRA);
        when(service.updateRemoteUrunBilgileri(eq(KREDI_NUMARASI), eq(SIRA), any(UrunBilgileriDTO.class)))
                .thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/remote/urunler/{krediNumarasi}/{sira}", KREDI_NUMARASI, SIRA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Güncellenecek ürün bulunamadı."));

        verify(service, times(1)).updateRemoteUrunBilgileri(eq(KREDI_NUMARASI), eq(SIRA), any(UrunBilgileriDTO.class));
    }

    // DELETE /remote/urunler/delete-and-reinsert-state-info-by-kredi
    @Test
    void deleteAndReinsert_shouldReturnSuccessMessageWithSira() throws Exception {
        // Arrange
        String successMessage = "Başarıyla silindi ve yeniden eklendi.";
        when(service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(KREDI_NUMARASI, SIRA))
                .thenReturn(successMessage);

        // Act & Assert
        mockMvc.perform(delete("/remote/urunler/delete-and-reinsert-state-info-by-kredi")
                        .param("krediNumarasi", KREDI_NUMARASI)
                        .param("sira", String.valueOf(SIRA)))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));

        verify(service, times(1)).deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(KREDI_NUMARASI, SIRA);
    }

    @Test
    void deleteAndReinsert_shouldReturnSuccessMessageWithoutSira() throws Exception {
        // Arrange
        String successMessage = "Başarıyla silindi ve yeniden eklendi.";
        when(service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(KREDI_NUMARASI, null))
                .thenReturn(successMessage);

        // Act & Assert
        mockMvc.perform(delete("/remote/urunler/delete-and-reinsert-state-info-by-kredi")
                        .param("krediNumarasi", KREDI_NUMARASI))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));

        verify(service, times(1)).deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(KREDI_NUMARASI, null);
    }

    @Test
    void deleteAndReinsert_shouldThrowNotFoundException() throws Exception {
        // Arrange
        String notFoundMessage = "Girilen kredi numarasına ait ürün bilgisi bulunamadı.";
        when(service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(KREDI_NUMARASI, null))
                .thenReturn(notFoundMessage);

        // Act & Assert
        mockMvc.perform(delete("/remote/urunler/delete-and-reinsert-state-info-by-kredi")
                        .param("krediNumarasi", KREDI_NUMARASI))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(notFoundMessage));

        verify(service, times(1)).deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(KREDI_NUMARASI, null);
    }

    // GET /remote/kootoevrakdurum
    @Test
    void getAllKoOtoEvrakDurum_shouldReturnListOfDTOs() throws Exception {
        // Arrange
        List<KO_OtoEvrakDurumDTO> list = List.of(
                createKoOtoEvrakDurumDTO(KREDI_NUMARASI, EVRAK_KODU),
                createKoOtoEvrakDurumDTO(KREDI_NUMARASI + "B", EVRAK_KODU + "B")
        );
        when(service.getAllRemoteKoOtoEvrakDurum()).thenReturn(list);

        // Act & Assert
        mockMvc.perform(get("/remote/kootoevrakdurum"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].krediNumarasi").value(KREDI_NUMARASI));

        verify(service, times(1)).getAllRemoteKoOtoEvrakDurum();
    }

    @Test
    void getAllKoOtoEvrakDurum_shouldThrowNotFoundException() throws Exception {
        // Arrange
        when(service.getAllRemoteKoOtoEvrakDurum()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/remote/kootoevrakdurum"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Evrak durumu verisi bulunamadı."));

        verify(service, times(1)).getAllRemoteKoOtoEvrakDurum();
    }

    // GET /remote/kootoevrakdurum/{krediNumarasi}
    @Test
    void getKoOtoEvrakDurumByKrediNumarasi_shouldReturnListOfDTOs() throws Exception {
        // Arrange
        List<KO_OtoEvrakDurumDTO> list = List.of(
                createKoOtoEvrakDurumDTO(KREDI_NUMARASI, EVRAK_KODU),
                createKoOtoEvrakDurumDTO(KREDI_NUMARASI, EVRAK_KODU + "B")
        );
        when(service.getRemoteKoOtoEvrakDurumByKrediNumarasi(KREDI_NUMARASI)).thenReturn(list);

        // Act & Assert
        mockMvc.perform(get("/remote/kootoevrakdurum/{krediNumarasi}", KREDI_NUMARASI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].krediNumarasi").value(KREDI_NUMARASI));

        verify(service, times(1)).getRemoteKoOtoEvrakDurumByKrediNumarasi(KREDI_NUMARASI);
    }

    @Test
    void getKoOtoEvrakDurumByKrediNumarasi_shouldThrowNotFoundException() throws Exception {
        // Arrange
        when(service.getRemoteKoOtoEvrakDurumByKrediNumarasi(KREDI_NUMARASI)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/remote/kootoevrakdurum/{krediNumarasi}", KREDI_NUMARASI))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Belirtilen kredi numarasına ait evrak durumu verisi bulunamadı."));

        verify(service, times(1)).getRemoteKoOtoEvrakDurumByKrediNumarasi(KREDI_NUMARASI);
    }

    // PUT /remote/kootoevrakdurum/update/{krediNumarasi}/{evrakKodu}
    @Test
    void updateKoOtoEvrakDurum_shouldReturnUpdatedDTO() throws Exception {
        // Arrange
        KO_OtoEvrakDurumDTO updatedDTO = createKoOtoEvrakDurumDTO(KREDI_NUMARASI, EVRAK_KODU);
        updatedDTO.setDurum(2);

        when(service.updateRemoteKoOtoEvrakDurumByKrediAndEvrakKodu(eq(KREDI_NUMARASI), eq(EVRAK_KODU), any(KO_OtoEvrakDurumDTO.class)))
                .thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/remote/kootoevrakdurum/update/{krediNumarasi}/{evrakKodu}", KREDI_NUMARASI, EVRAK_KODU)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.durum").value(2));

        verify(service, times(1)).updateRemoteKoOtoEvrakDurumByKrediAndEvrakKodu(eq(KREDI_NUMARASI), eq(EVRAK_KODU), any(KO_OtoEvrakDurumDTO.class));
    }

    @Test
    void updateKoOtoEvrakDurum_shouldThrowNotFoundException() throws Exception {
        // Arrange
        KO_OtoEvrakDurumDTO updateData = createKoOtoEvrakDurumDTO(KREDI_NUMARASI, EVRAK_KODU);
        when(service.updateRemoteKoOtoEvrakDurumByKrediAndEvrakKodu(eq(KREDI_NUMARASI), eq(EVRAK_KODU), any(KO_OtoEvrakDurumDTO.class)))
                .thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/remote/kootoevrakdurum/update/{krediNumarasi}/{evrakKodu}", KREDI_NUMARASI, EVRAK_KODU)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Güncellenecek evrak durumu bulunamadı."));

        verify(service, times(1)).updateRemoteKoOtoEvrakDurumByKrediAndEvrakKodu(eq(KREDI_NUMARASI), eq(EVRAK_KODU), any(KO_OtoEvrakDurumDTO.class));
    }

    // DELETE /remote/kogunkapama/process/{date}
    @Test
    void processKoGunKapamaByDate_shouldReturnOkStatus() throws Exception {
        // Arrange
        LocalDate today = LocalDate.now();
        doNothing().when(service).processRemoteKoGunKapamaByDate(today);

        // Act & Assert
        mockMvc.perform(delete("/remote/kogunkapama/process/{date}", today.toString()))
                .andExpect(status().isOk());

        verify(service, times(1)).processRemoteKoGunKapamaByDate(today);
    }
}
