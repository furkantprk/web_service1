package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrunBilgileriDTO {
    private String krediNumarasi;
    private Integer sira;
    private Integer rehinDurum;
    private Long productLineId; // Tipi Integer'dan Long'a güncellendi
}