package com.example.demo.controller;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.service.UrunBilgileriRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; // Burayı ekledik
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/remote") // Remote path'i genel olarak belirttik
public class UrunBilgileriRemoteController {

    private final UrunBilgileriRemoteService service;

    @GetMapping("/urunler")
    public List<UrunBilgileriDTO> getRemoteUrunler() {
        return service.getRemoteUrunler();
    }

    // Kredi numarasına göre uzaktan ürün arama endpoint'i
    // GET http://localhost:8080/remote/urunler/kredi/{krediNumarasi}
    @GetMapping("/urunler/kredi/{krediNumarasi}")
    public ResponseEntity<List<UrunBilgileriDTO>> getRemoteUrunlerByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<UrunBilgileriDTO> urunler = service.getRemoteUrunlerByKrediNumarasi(krediNumarasi);
        if (urunler.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(urunler);
    }

    // Uzaktan ürün bilgilerini güncelleme endpoint'i
    // PUT http://localhost:8080/remote/urunler/{krediNumarasi}/{sira}
    // Request Body: { "rehinDurum": 1, "productLineId": 123 }
    @PutMapping("/urunler/{krediNumarasi}/{sira}")
    public ResponseEntity<UrunBilgileriDTO> updateRemoteUrunBilgileri(
            @PathVariable String krediNumarasi,
            @PathVariable Integer sira,
            @RequestBody UrunBilgileriDTO urunBilgileriDTO) {
        UrunBilgileriDTO updated = service.updateRemoteUrunBilgileri(krediNumarasi, sira, urunBilgileriDTO);
        if (updated == null) {
            return ResponseEntity.notFound().build(); // Eğer 8081'den null dönerse
        }
        return ResponseEntity.ok(updated);
    }
}