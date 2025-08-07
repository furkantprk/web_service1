package com.example.demo.service;

import com.example.demo.dto.KO_OtoEvrakDurumDTO;
import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.feign.UrunBilgileriFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
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

    @Test
    void testGetRemoteKoOtoEvrakDurumByKrediNumarasi() {
        String kredi = "123";
        when(feignClient.getKoOtoEvrakDurumByKrediNumarasi(kredi)).thenReturn(List.of(new KO_OtoEvrakDurumDTO()));
        List<KO_OtoEvrakDurumDTO> result = service.getRemoteKoOtoEvrakDurumByKrediNumarasi(kredi);
        assertThat(result).isNotEmpty();
        verify(feignClient).getKoOtoEvrakDurumByKrediNumarasi(kredi);
    }

    @Test
    void testGetAllRemoteKoOtoEvrakDurum() {
        when(feignClient.getAllKoOtoEvrakDurum()).thenReturn(List.of(new KO_OtoEvrakDurumDTO()));
        List<KO_OtoEvrakDurumDTO> result = service.getAllRemoteKoOtoEvrakDurum();
        assertThat(result).isNotEmpty();
        verify(feignClient).getAllKoOtoEvrakDurum();
    }

    @Test
    void testUpdateRemoteKoOtoEvrakDurumByKrediAndEvrakKodu() {
        String kredi = "123";
        String evrakKodu = "testEvrak";
        KO_OtoEvrakDurumDTO dto = new KO_OtoEvrakDurumDTO();
        when(feignClient.updateKoOtoEvrakDurumByKrediAndEvrakKodu(kredi, evrakKodu, dto)).thenReturn(dto);
        KO_OtoEvrakDurumDTO result = service.updateRemoteKoOtoEvrakDurumByKrediAndEvrakKodu(kredi, evrakKodu, dto);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void testProcessRemoteKoGunKapamaByDate() {
        LocalDate date = LocalDate.now();
        service.processRemoteKoGunKapamaByDate(date);
        verify(feignClient).processKoGunKapamaByDate(date);
    }
}
