// dil: java
package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SmsRecordDTO {
    private String phoneNumber;
    private String messageBody;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate insertDate; // Tip LocalDate olarak güncellendi.

    private String smsKod;
    private String gonderilenProg;
    private String kaynakTablo;
}