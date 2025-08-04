package com.example.demo.service;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.feign.UrunBilgileriFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq; // 'eq' için import eklendi
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
    void testGetRemoteUrunBilgileri_returnsList() {
        UrunBilgileriDTO sample = new UrunBilgileriDTO();
        when(feignClient.getUrunBilgileri()).thenReturn(List.of(sample));

        List<UrunBilgileriDTO> result = remoteService.getRemoteUrunBilgileri();
        assertEquals(1, result.size());
        verify(feignClient, times(1)).getUrunBilgileri();
    }

    @Test
    void testGetRemoteUrunlerByKrediNumarasi_returnsFilteredList() {
        String krediNumarasi = "12345";
        UrunBilgileriDTO dto = new UrunBilgileriDTO();
        when(feignClient.getUrunBilgileriByKrediNumarasi(krediNumarasi)).thenReturn(List.of(dto));

        List<UrunBilgileriDTO> result = remoteService.getRemoteUrunlerByKrediNumarasi(krediNumarasi);
        assertFalse(result.isEmpty());
        verify(feignClient).getUrunBilgileriByKrediNumarasi(krediNumarasi);
    }

    @Test
    void testGetRemoteSiralarByKrediNumarasi_returnsListOfSiralar() {
        String krediNumarasi = "KREDI_SIRALARI";
        List<Integer> expectedSiralar = Arrays.asList(1, 2, 3);
        when(feignClient.getSiralarByKrediNumarasi(krediNumarasi)).thenReturn(expectedSiralar);

        List<Integer> result = remoteService.getRemoteSiralarByKrediNumarasi(krediNumarasi);

        assertEquals(expectedSiralar.size(), result.size());
        assertEquals(expectedSiralar.get(0), result.get(0));
        verify(feignClient, times(1)).getSiralarByKrediNumarasi(krediNumarasi);
    }

    @Test
    void testGetRemoteSiralarByKrediNumarasi_returnsEmptyListWhenNoSiralar() {
        String krediNumarasi = "NO_SIRAS";
        when(feignClient.getSiralarByKrediNumarasi(krediNumarasi)).thenReturn(Collections.emptyList());

        List<Integer> result = remoteService.getRemoteSiralarByKrediNumarasi(krediNumarasi);

        assertTrue(result.isEmpty());
        verify(feignClient, times(1)).getSiralarByKrediNumarasi(krediNumarasi);
    }

    @Test
    void testUpdateRemoteUrunBilgileri_returnsUpdatedDTO() {
        String krediNumarasi = "45678";
        int sira = 1;
        UrunBilgileriDTO inputDto = new UrunBilgileriDTO();
        UrunBilgileriDTO updatedDto = new UrunBilgileriDTO();
        when(feignClient.updateUrunBilgileri(krediNumarasi, sira, inputDto)).thenReturn(updatedDto);

        UrunBilgileriDTO result = remoteService.updateRemoteUrunBilgileri(krediNumarasi, sira, inputDto);
        assertEquals(updatedDto, result);
        verify(feignClient).updateUrunBilgileri(krediNumarasi, sira, inputDto);
    }

    @Test
    void testDeleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira_returnsSuccessMessageForSpecificSira() {
        String krediNumarasi = "KREDI999";
        Integer sira = 5;
        String expectedMessage = "Kredi numarası: KREDI999 ve sıra: 5 için EgmStateInformation başarıyla güncellendi.";
        // Feign client metodunun çağrıldığında beklenen mesajı döndürmesini mock'la
        when(feignClient.deleteAndReinsertEgmStateInformationByKrediNumarasi(eq(krediNumarasi), eq(sira)))
                .thenReturn(expectedMessage);

        String result = remoteService.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);

        assertEquals(expectedMessage, result);
        // Feign client metodunun doğru kredi numarası ve sıra numarasıyla çağrıldığını doğrula
        verify(feignClient, times(1)).deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi, sira);
    }

    @Test
    void testDeleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira_returnsSuccessMessageForAllSiralar() {
        String krediNumarasi = "KREDIALL";
        Integer sira = null; // 'Tümünü Seç' durumu, null olarak geçecek
        String expectedMessage = "Kredi numarası: KREDIALL için EgmStateInformation başarıyla güncellendi.";
        when(feignClient.deleteAndReinsertEgmStateInformationByKrediNumarasi(eq(krediNumarasi), eq(null))) // null bekliyoruz
                .thenReturn(expectedMessage);

        String result = remoteService.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);

        assertEquals(expectedMessage, result);
        verify(feignClient, times(1)).deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi, null);
    }

    @Test
    void testDeleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira_handlesException() {
        String krediNumarasi = "KREDI_INVALID";
        Integer sira = 10;
        when(feignClient.deleteAndReinsertEgmStateInformationByKrediNumarasi(eq(krediNumarasi), eq(sira)))
                .thenThrow(new RuntimeException("Uzak servis hatası"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            remoteService.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);
        });

        assertTrue(thrown.getMessage().contains("Uzak servis hatası"));
        verify(feignClient, times(1)).deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi, sira);
    }
}