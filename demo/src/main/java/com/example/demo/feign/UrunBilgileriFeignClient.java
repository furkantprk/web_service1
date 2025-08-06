package com.example.demo.feign;

import com.example.demo.dto.KO_OtoEvrakDurumDTO;
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

    @GetMapping("/api/urunler/siralar/{krediNumarasi}")
    List<Integer> getSiralarByKrediNumarasi(@PathVariable("krediNumarasi") String krediNumarasi);

    @PutMapping("/api/urunler/{krediNumarasi}/{sira}")
    UrunBilgileriDTO updateUrunBilgileri(@PathVariable("krediNumarasi") String krediNumarasi, @PathVariable("sira") Integer sira, @RequestBody UrunBilgileriDTO urunBilgileriDTO);

    @DeleteMapping("/api/urunler/delete-and-reinsert-state-info-by-kredi")
    String deleteAndReinsertEgmStateInformationByKrediNumarasi(@RequestParam("krediNumarasi") String krediNumarasi,
                                                               @RequestParam(name = "sira", required = false) Integer sira);

    // Yeni endpoint, sadece belirli bir kredi numarasına ait evrakları getirir
    @GetMapping("/api/urunler/kootoevrakdurum/kredi/{krediNumarasi}")
    List<KO_OtoEvrakDurumDTO> getKoOtoEvrakDurumByKrediNumarasi(@PathVariable("krediNumarasi") String krediNumarasi);

    // Mevcut endpoint, tüm evrakları getirir
    @GetMapping("/api/urunler/kootoevrakdurum")
    List<KO_OtoEvrakDurumDTO> getAllKoOtoEvrakDurum();

    // Evrak durumu güncelle
    @PutMapping("/api/urunler/kootoevrakdurum/update/{krediNumarasi}/{evrakKodu}")
    KO_OtoEvrakDurumDTO updateKoOtoEvrakDurumByKrediAndEvrakKodu(
            @PathVariable("krediNumarasi") String krediNumarasi,
            @PathVariable("evrakKodu") String evrakKodu,
            @RequestBody KO_OtoEvrakDurumDTO updateData);
}