package com.example.demo.service;

import com.example.demo.dto.KO_OtoEvrakDurumDTO;
import com.example.demo.dto.UrunBilgileriDTO;
import com.example.demo.feign.UrunBilgileriFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UrunBilgileriRemoteService {

    private final UrunBilgileriFeignClient feignClient;

    public List<UrunBilgileriDTO> getRemoteUrunBilgileri() {
        return feignClient.getUrunBilgileri();
    }

    public List<UrunBilgileriDTO> getRemoteUrunlerByKrediNumarasi(String krediNumarasi) {
        return feignClient.getUrunBilgileriByKrediNumarasi(krediNumarasi);
    }

    public List<Integer> getRemoteSiralarByKrediNumarasi(String krediNumarasi) {
        return feignClient.getSiralarByKrediNumarasi(krediNumarasi);
    }

    public UrunBilgileriDTO updateRemoteUrunBilgileri(String krediNumarasi, Integer sira, UrunBilgileriDTO urunBilgileriDTO) {
        return feignClient.updateUrunBilgileri(krediNumarasi, sira, urunBilgileriDTO);
    }

    public String deleteAndReinsertRemoteEgmStateInformationByKrediNumarasiAndSira(String krediNumarasi, Integer sira) {
        return feignClient.deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi, sira);
    }

    // 🔹 Yeni metot: Belirli bir kredi numarasına ait evrakları getirir
    public List<KO_OtoEvrakDurumDTO> getRemoteKoOtoEvrakDurumByKrediNumarasi(String krediNumarasi) {
        return feignClient.getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);
    }

    public List<KO_OtoEvrakDurumDTO> getAllRemoteKoOtoEvrakDurum() {
        return feignClient.getAllKoOtoEvrakDurum();
    }

    // 🔹 Evrak durumu güncelleme
    public KO_OtoEvrakDurumDTO updateRemoteKoOtoEvrakDurumByKrediAndEvrakKodu(
            String krediNumarasi,
            String evrakKodu,
            KO_OtoEvrakDurumDTO updateData) {
        return feignClient.updateKoOtoEvrakDurumByKrediAndEvrakKodu(krediNumarasi, evrakKodu, updateData);
    }
}