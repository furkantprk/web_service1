package com.example.demo.service;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.feign.UrunBilgileriFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UrunBilgileriRemoteService {

    private final UrunBilgileriFeignClient feignClient;

    public List<UrunBilgileriDTO> getRemoteUrunBilgileri() {
        return feignClient.getUrunBilgileri();
    }

    public List<UrunBilgileriDTO> getRemoteUrunlerByKrediNumarasi(String krediNumarasi) {
        return feignClient.getUrunBilgileriByKrediNumarasi(krediNumarasi);
    }

    // YENİ SERVİS METOT: Kredi numarasına ait sıra numaralarını getirir
    public List<Integer> getRemoteSiralarByKrediNumarasi(String krediNumarasi) {
        return feignClient.getSiralarByKrediNumarasi(krediNumarasi);
    }

    public UrunBilgileriDTO updateRemoteUrunBilgileri(String krediNumarasi, Integer sira, UrunBilgileriDTO urunBilgileriDTO) {
        return feignClient.updateUrunBilgileri(krediNumarasi, sira, urunBilgileriDTO);
    }

    // GÜNCELLENMİŞ SERVİS METOT: Kredi numarası ve isteğe bağlı sıra numarası ile işlem yapacak
    public String deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(String krediNumarasi, Integer sira) {
        return feignClient.deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi, sira);
    }
}