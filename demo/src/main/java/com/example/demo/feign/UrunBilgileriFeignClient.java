package com.example.demo.feign;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "urun-bilgileri-client", url = "https://kf-proje1.onrender.com", configuration = FeignConfig.class)
public interface UrunBilgileriFeignClient {

    @GetMapping("/api/urunler")
    List<UrunBilgileriDTO> getUrunBilgileri();

    @GetMapping("/api/urunler/kredi/{krediNumarasi}")
    List<UrunBilgileriDTO> getUrunBilgileriByKrediNumarasi(@PathVariable("krediNumarasi") String krediNumarasi);

    @PutMapping("/api/urunler/{krediNumarasi}/{sira}")
    UrunBilgileriDTO updateUrunBilgileri(@PathVariable("krediNumarasi") String krediNumarasi, @PathVariable("sira") Integer sira, @RequestBody UrunBilgileriDTO urunBilgileriDTO);


    @DeleteMapping("/api/urunler/delete-and-reinsert-state-info-by-kredi/{krediNumarasi}")
    String deleteAndReinsertEgmStateInformationByKrediNumarasi(@PathVariable("krediNumarasi") String krediNumarasi);
}