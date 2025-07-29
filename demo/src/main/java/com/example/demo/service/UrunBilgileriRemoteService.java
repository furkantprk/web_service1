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

    // Kredi numarasına göre uzaktan ürünleri getiren metod
    public List<UrunBilgileriDTO> getRemoteUrunlerByKrediNumarasi(String krediNumarasi) {
        return feignClient.getUrunlerByKrediNumarasi(krediNumarasi);
    }

    // Uzaktan ürün bilgilerini güncelleyen metod
    public UrunBilgileriDTO updateRemoteUrunBilgileri(String krediNumarasi, Integer sira, UrunBilgileriDTO urunBilgileriDTO) {
        return feignClient.updateUrunBilgileri(krediNumarasi, sira, urunBilgileriDTO);
    }
}