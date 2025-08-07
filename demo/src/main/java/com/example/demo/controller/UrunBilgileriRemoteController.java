package com.example.demo.controller;

import com.example.demo.dto.KO_OtoEvrakDurumDTO;
import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.expection.RemoteServiceNotFoundException;
import com.example.demo.service.UrunBilgileriRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    // --- ✅ EKLENENLER ---

    @GetMapping("/kootoevrakdurum")
    public ResponseEntity<List<KO_OtoEvrakDurumDTO>> getAllKoOtoEvrakDurum() {
        List<KO_OtoEvrakDurumDTO> list = service.getAllRemoteKoOtoEvrakDurum();
        if (list.isEmpty()) {
            throw new RemoteServiceNotFoundException("Evrak durumu verisi bulunamadı.");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/kootoevrakdurum/{krediNumarasi}")
    public ResponseEntity<List<KO_OtoEvrakDurumDTO>> getKoOtoEvrakDurumByKrediNumarasi(
            @PathVariable String krediNumarasi) {
        List<KO_OtoEvrakDurumDTO> list = service.getRemoteKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);
        if (list.isEmpty()) {
            throw new RemoteServiceNotFoundException("Belirtilen kredi numarasına ait evrak durumu verisi bulunamadı.");
        }
        return ResponseEntity.ok(list);
    }

    @PutMapping("/kootoevrakdurum/update/{krediNumarasi}/{evrakKodu}")
    public ResponseEntity<KO_OtoEvrakDurumDTO> updateKoOtoEvrakDurumByKrediAndEvrakKodu(
            @PathVariable String krediNumarasi,
            @PathVariable String evrakKodu,
            @RequestBody KO_OtoEvrakDurumDTO updateData) {
        KO_OtoEvrakDurumDTO updated = service.updateRemoteKoOtoEvrakDurumByKrediAndEvrakKodu(krediNumarasi, evrakKodu, updateData);
        if (updated == null) {
            throw new RemoteServiceNotFoundException("Güncellenecek evrak durumu bulunamadı.");
        }
        return ResponseEntity.ok(updated);
    }

    // 🔹 Yeni metot: Arkadaşının API'sindeki günlük kapanış işlemini çağırır
    @DeleteMapping("/kogunkapama/process/{date}")
    public ResponseEntity<Void> processKoGunKapamaByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        service.processRemoteKoGunKapamaByDate(date);
        return ResponseEntity.ok().build();
    }
}