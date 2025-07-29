package com.example.demo.feign;

import com.example.demo.dto.UrunBilgileriDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "urun-bilgileri-client", url = "https://kf-proje1.onrender.com")
public interface UrunBilgileriFeignClient {

    @GetMapping("/api/urunler")
    List<UrunBilgileriDTO> getUrunler();

    // Kredi numarasına göre ürünleri getiren yeni metod
    @GetMapping("/api/urunler/kredi/{krediNumarasi}")
    List<UrunBilgileriDTO> getUrunlerByKrediNumarasi(@PathVariable String krediNumarasi);

    // Ürün bilgilerini güncelleyen yeni metod
    @PutMapping("/api/urunler/{krediNumarasi}/{sira}")
    UrunBilgileriDTO updateUrunBilgileri(
            @PathVariable String krediNumarasi,
            @PathVariable Integer sira,
            @RequestBody UrunBilgileriDTO urunBilgileriDTO);
}