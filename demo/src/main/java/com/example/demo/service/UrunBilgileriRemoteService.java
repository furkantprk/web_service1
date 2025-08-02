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

    public List<UrunBilgileriDTO> getRemoteUrunler() {
        return feignClient.getUrunler();
    }

    public List<UrunBilgileriDTO> getRemoteUrunlerByKrediNumarasi(String krediNumarasi) {
        return feignClient.getUrunlerByKrediNumarasi(krediNumarasi);
    }

    public UrunBilgileriDTO updateRemoteUrunBilgileri(String krediNumarasi, Integer sira, UrunBilgileriDTO urunBilgileriDTO) {
        return feignClient.updateUrunBilgileri(krediNumarasi, sira, urunBilgileriDTO);
    }

    // Yeni servis metodu: Uzak servis üzerinden silme ve yeniden ekleme işlemini çağırır
    public String deleteAndReinsertRemoteEgmStateInformation(Long productLineId) {
        return feignClient.deleteAndReinsertEgmStateInformation(productLineId);
    }
}