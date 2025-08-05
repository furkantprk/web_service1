package com.example.demo.service;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.feign.UrunBilgileriFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UrunBilgileriRemoteServiceTest {

    private UrunBilgileriFeignClient feignClient;
    private UrunBilgileriRemoteService service;

    @BeforeEach
    void setUp() {
        feignClient = Mockito.mock(UrunBilgileriFeignClient.class);
        service = new UrunBilgileriRemoteService(feignClient);
    }

    @Test
    void testGetRemoteUrunBilgileri() {
        when(feignClient.getUrunBilgileri()).thenReturn(List.of(new UrunBilgileriDTO()));
        List<UrunBilgileriDTO> result = service.getRemoteUrunBilgileri();
        assertThat(result).isNotNull();
        verify(feignClient).getUrunBilgileri();
    }

    @Test
    void testGetRemoteUrunlerByKrediNumarasi() {
        String kredi = "123";
        when(feignClient.getUrunBilgileriByKrediNumarasi(kredi)).thenReturn(List.of(new UrunBilgileriDTO()));
        List<UrunBilgileriDTO> result = service.getRemoteUrunlerByKrediNumarasi(kredi);
        assertThat(result).isNotEmpty();
        verify(feignClient).getUrunBilgileriByKrediNumarasi(kredi);
    }

    @Test
    void testGetRemoteSiralarByKrediNumarasi() {
        String kredi = "123";
        when(feignClient.getSiralarByKrediNumarasi(kredi)).thenReturn(List.of(1, 2));
        List<Integer> result = service.getRemoteSiralarByKrediNumarasi(kredi);
        assertThat(result).contains(1, 2);
    }

    @Test
    void testUpdateRemoteUrunBilgileri() {
        String kredi = "123";
        Integer sira = 1;
        UrunBilgileriDTO dto = new UrunBilgileriDTO();
        when(feignClient.updateUrunBilgileri(kredi, sira, dto)).thenReturn(dto);
        UrunBilgileriDTO result = service.updateRemoteUrunBilgileri(kredi, sira, dto);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void testDeleteAndReinsertRemoteEgmStateInformation() {
        String kredi = "123";
        Integer sira = 5;
        when(feignClient.deleteAndReinsertEgmStateInformationByKrediNumarasi(kredi, sira))
                .thenReturn("Başarılı");
        String result = service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(kredi, sira);
        assertThat(result).isEqualTo("Başarılı");
    }
}
