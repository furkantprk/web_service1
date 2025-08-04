package com.example.demo.service;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.feign.UrunBilgileriFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UrunBilgileriRemoteServiceTest {

    private UrunBilgileriFeignClient feignClient;
    private UrunBilgileriRemoteService remoteService;

    @BeforeEach
    void setUp() {
        feignClient = mock(UrunBilgileriFeignClient.class);
        remoteService = new UrunBilgileriRemoteService(feignClient);
    }

    @Test
    void testGetRemoteUrunBilgileri_returnsList() { // Metot adı güncellendi
        UrunBilgileriDTO sample = new UrunBilgileriDTO();
        when(feignClient.getUrunBilgileri()).thenReturn(List.of(sample)); // Feign client metot adı güncellendi

        List<UrunBilgileriDTO> result = remoteService.getRemoteUrunBilgileri(); // Servis metot adı güncellendi
        assertEquals(1, result.size());
        verify(feignClient, times(1)).getUrunBilgileri(); // Feign client metot adı güncellendi
    }

    @Test
    void testGetRemoteUrunlerByKrediNumarasi_returnsFilteredList() {
        String krediNumarasi = "12345";
        UrunBilgileriDTO dto = new UrunBilgileriDTO();
        when(feignClient.getUrunBilgileriByKrediNumarasi(krediNumarasi)).thenReturn(List.of(dto)); // Feign client metot adı güncellendi

        List<UrunBilgileriDTO> result = remoteService.getRemoteUrunlerByKrediNumarasi(krediNumarasi);
        assertFalse(result.isEmpty());
        verify(feignClient).getUrunBilgileriByKrediNumarasi(krediNumarasi); // Feign client metot adı güncellendi
    }

    @Test
    void testUpdateRemoteUrunBilgileri_returnsUpdatedDTO() {
        String krediNumarasi = "45678";
        int sira =1;
        UrunBilgileriDTO inputDto = new UrunBilgileriDTO();
        UrunBilgileriDTO updatedDto = new UrunBilgileriDTO();
        when(feignClient.updateUrunBilgileri(krediNumarasi, sira, inputDto)).thenReturn(updatedDto);

        UrunBilgileriDTO result = remoteService.updateRemoteUrunBilgileri(krediNumarasi, sira, inputDto);
        assertEquals(updatedDto, result);
        verify(feignClient).updateUrunBilgileri(krediNumarasi, sira, inputDto);
    }

    @Test
    void testDeleteAndReinsertRemoteEgmStateInformationByKrediNumarasi_returnsSuccessMessage() {
        String krediNumarasi = "KREDI999";
        String expectedMessage = "İşlem başarıyla tamamlandı.";
        // Feign client metodunun çağrıldığında beklenen mesajı döndürmesini mock'la
        when(feignClient.deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi))
                .thenReturn(expectedMessage);

        String result = remoteService.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasi(krediNumarasi);

        assertEquals(expectedMessage, result);
        // Feign client metodunun doğru kredi numarasıyla çağrıldığını doğrula
        verify(feignClient, times(1)).deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi);
    }

    @Test
    void testDeleteAndReinsertRemoteEgmStateInformationByKrediNumarasi_handlesException() {
        String krediNumarasi = "KREDI_INVALID";
        // Feign client metodunun bir hata fırlatmasını mock'la
        when(feignClient.deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi))
                .thenThrow(new RuntimeException("Uzak servis hatası"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            remoteService.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasi(krediNumarasi);
        });

        assertTrue(thrown.getMessage().contains("Uzak servis hatası"));
        verify(feignClient, times(1)).deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi);
    }
}