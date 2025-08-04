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

    public UrunBilgileriDTO updateRemoteUrunBilgileri(String krediNumarasi, Integer sira, UrunBilgileriDTO urunBilgileriDTO) {
        return feignClient.updateUrunBilgileri(krediNumarasi, sira, urunBilgileriDTO);
    }

    // SADECE BU SERVİS METOT KULLANILACAK: Kredi numarasına göre işlem yapacak
    public String deleteAndReinsertRemoteEgmStateInformationByKrediNumarasi(String krediNumarasi) {
        return feignClient.deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi);
    }
}