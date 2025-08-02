package com.example.demo.feign;

import com.example.demo.dto.UrunBilgileriDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping; // Yeni! Delete mapping için
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// FeignClient, kf-proje1.onrender.com adresindeki servise bağlanacak
@FeignClient(name = "urun-bilgileri-client", url = "https://kf-proje1.onrender.com")
public interface UrunBilgileriFeignClient {

    @GetMapping("/api/urunler")
    List<UrunBilgileriDTO> getUrunler();

    @GetMapping("/api/urunler/kredi/{krediNumarasi}")
    List<UrunBilgileriDTO> getUrunlerByKrediNumarasi(@PathVariable String krediNumarasi);

    @PutMapping("/api/urunler/{krediNumarasi}/{sira}")
    UrunBilgileriDTO updateUrunBilgileri(
            @PathVariable String krediNumarasi,
            @PathVariable Integer sira,
            @RequestBody UrunBilgileriDTO urunBilgileriDTO);

    // Yeni Feign metodu: Uzak servisteki delete-and-reinsert-state-info endpoint'ini çağırır
    @DeleteMapping("/api/urunler/delete-and-reinsert-state-info/{productLineId}")
    String deleteAndReinsertEgmStateInformation(@PathVariable Long productLineId);
}