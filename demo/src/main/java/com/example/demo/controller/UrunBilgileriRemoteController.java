package com.example.demo.controller;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.service.UrunBilgileriRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/remote")
public class UrunBilgileriRemoteController {

    private final UrunBilgileriRemoteService service;

    @GetMapping("/urunler")
    public List<UrunBilgileriDTO> getUrunBilgileri() {
        return service.getRemoteUrunBilgileri();
    }

    @GetMapping("/urunler/kredi/{krediNumarasi}")
    public ResponseEntity<List<UrunBilgileriDTO>> getUrunlerByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<UrunBilgileriDTO> urunler = service.getRemoteUrunlerByKrediNumarasi(krediNumarasi);
        if (urunler.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(urunler);
    }

    // YENİ ENDPOINT: Kredi numarasına ait sıra numaralarını döndürür
    @GetMapping("/urunler/siralar/{krediNumarasi}")
    public ResponseEntity<List<Integer>> getSiralarByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<Integer> siralar = service.getRemoteSiralarByKrediNumarasi(krediNumarasi);
        if (siralar.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(siralar);
    }

    @PutMapping("/urunler/{krediNumarasi}/{sira}")
    public ResponseEntity<UrunBilgileriDTO> updateUrunBilgileri(
            @PathVariable String krediNumarasi,
            @PathVariable Integer sira,
            @RequestBody UrunBilgileriDTO urunBilgileriDTO) {
        try {
            UrunBilgileriDTO updated = service.updateRemoteUrunBilgileri(krediNumarasi, sira, urunBilgileriDTO);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // GÜNCELLENMİŞ DELETE ENDPOINT: Kredi numarası ve isteğe bağlı sıra numarası ile işlem yapacak
    // RequestParam kullandık, bu sayede URL'de ?krediNumarasi=X&sira=Y şeklinde gönderilecek.
    @DeleteMapping("/urunler/delete-and-reinsert-state-info-by-kredi")
    public ResponseEntity<String> deleteAndReinsertRemoteEgmStateInformationByKrediNumarasi(
            @RequestParam String krediNumarasi,
            @RequestParam(required = false) Integer sira) {
        try {
            // Servise hem kredi numarasını hem de sıra numarasını iletiyoruz
            String responseMessage = service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);
            if (responseMessage.contains("bulunamadı")) { // Mesaj içeriğine göre 404 dönüyoruz
                return ResponseEntity.status(404).body(responseMessage);
            }
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Uzak serviste işlem sırasında bir hata oluştu: " + e.getMessage());
        }
    }
}