package com.example.demo.controller;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.service.UrunBilgileriRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/remote") // Bu controller'ın tüm endpointleri /remote altında olacak
public class UrunBilgileriRemoteController {

    private final UrunBilgileriRemoteService service;

    @GetMapping("/urunler")
    public List<UrunBilgileriDTO> getRemoteUrunler() {
        return service.getRemoteUrunler();
    }

    @GetMapping("/urunler/kredi/{krediNumarasi}")
    public ResponseEntity<List<UrunBilgileriDTO>> getRemoteUrunlerByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<UrunBilgileriDTO> urunler = service.getRemoteUrunlerByKrediNumarasi(krediNumarasi);
        if (urunler.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(urunler);
    }

    @PutMapping("/urunler/{krediNumarasi}/{sira}")
    public ResponseEntity<UrunBilgileriDTO> updateRemoteUrunBilgileri(
            @PathVariable String krediNumarasi,
            @PathVariable Integer sira,
            @RequestBody UrunBilgileriDTO urunBilgileriDTO) {
        UrunBilgileriDTO updated = service.updateRemoteUrunBilgileri(krediNumarasi, sira, urunBilgileriDTO);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/urunler/delete-and-reinsert-state-info/{productLineId}")
    public ResponseEntity<String> deleteAndReinsertRemoteEgmStateInformation(@PathVariable Long productLineId) {
        try {
            String responseMessage = service.deleteAndReinsertRemoteEgmStateInformation(productLineId);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            // Feign Client'tan gelen hataları burada yakalayabilirsiniz
            return ResponseEntity.status(500).body("Uzak serviste işlem sırasında bir hata oluştu: " + e.getMessage());
        }
    }
}