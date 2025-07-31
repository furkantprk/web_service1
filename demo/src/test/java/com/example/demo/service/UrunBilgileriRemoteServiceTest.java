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
    void testGetRemoteUrunler_returnsList() {
        UrunBilgileriDTO sample = new UrunBilgileriDTO();
        when(feignClient.getUrunler()).thenReturn(List.of(sample));

        List<UrunBilgileriDTO> result = remoteService.getRemoteUrunler();
        assertEquals(1, result.size());
        verify(feignClient, times(1)).getUrunler();
    }

    @Test
    void testGetRemoteUrunlerByKrediNumarasi_returnsFilteredList() {
        String krediNumarasi = "12345";
        UrunBilgileriDTO dto = new UrunBilgileriDTO();
        when(feignClient.getUrunlerByKrediNumarasi(krediNumarasi)).thenReturn(List.of(dto));

        List<UrunBilgileriDTO> result = remoteService.getRemoteUrunlerByKrediNumarasi(krediNumarasi);
        assertFalse(result.isEmpty());
        verify(feignClient).getUrunlerByKrediNumarasi(krediNumarasi);
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
}
