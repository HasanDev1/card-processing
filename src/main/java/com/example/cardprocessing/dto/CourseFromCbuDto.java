package com.example.cardprocessing.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseFromCbuDto {

    private String id;

    @JsonAlias("Code")
    private String code;

    @JsonAlias("Ccy")
    private String ccy;

    @JsonAlias("CcyNm_RU")
    private String ccyNmRU;

    @JsonAlias("CcyNm_UZ")
    private String ccyNmUZ;

    @JsonAlias("CcyNm_UZC")
    private String ccyNmUZC;

    @JsonAlias("CcyNm_EN")
    private String ccyNmEN;

    @JsonAlias("Nominal")
    private String nominal;

    @JsonAlias("Rate")
    private String rate;

    @JsonAlias("Diff")
    private String diff;

    @JsonAlias("Date")
    private String date;
}
