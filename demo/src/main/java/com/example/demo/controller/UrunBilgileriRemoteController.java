package com.example.demo.controller;

import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.expection.RemoteServiceNotFoundException;
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
            throw new RemoteServiceNotFoundException("Girilen kredi numarasına ait ürün bilgisi bulunamadı.");
        }
        return ResponseEntity.ok(urunler);
    }

    @GetMapping("/urunler/siralar/{krediNumarasi}")
    public ResponseEntity<List<Integer>> getSiralarByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<Integer> siralar = service.getRemoteSiralarByKrediNumarasi(krediNumarasi);
        if (siralar.isEmpty()) {
            throw new RemoteServiceNotFoundException("Girilen kredi numarasına ait sıra bilgisi bulunamadı.");
        }
        return ResponseEntity.ok(siralar);
    }

    @PutMapping("/urunler/{krediNumarasi}/{sira}")
    public ResponseEntity<UrunBilgileriDTO> updateUrunBilgileri(
            @PathVariable String krediNumarasi,
            @PathVariable Integer sira,
            @RequestBody UrunBilgileriDTO urunBilgileriDTO) {
        UrunBilgileriDTO updated = service.updateRemoteUrunBilgileri(krediNumarasi, sira, urunBilgileriDTO);
        if (updated == null) {
            throw new RemoteServiceNotFoundException("Güncellenecek ürün bulunamadı.");
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/urunler/delete-and-reinsert-state-info-by-kredi")
    public ResponseEntity<String> deleteAndReinsertRemoteEgmStateInformationByKrediNumarasi(
            @RequestParam String krediNumarasi,
            @RequestParam(required = false) Integer sira) {
        String responseMessage = service.deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);

        if (responseMessage.contains("bulunamadı")) {
            throw new RemoteServiceNotFoundException(responseMessage);
        }

        return ResponseEntity.ok(responseMessage);
    }
}
