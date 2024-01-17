package com.fastcampus.loan.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class AcceptTermsDTO1 implements Serializable {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class Request {

        private String name;

        private String cellPhone;

        private String email;

        private BigDecimal hopeAmount;

        private BigDecimal interestRate;


    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class AcceptTerms {

        List<Long> acceptTermsIds;
    }
}
