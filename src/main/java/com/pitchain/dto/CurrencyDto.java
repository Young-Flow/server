package com.pitchain.dto;

import lombok.Getter;
import lombok.Setter;

public class CurrencyDto {

    @Getter
    @Setter
    public static class ResponseDto {
        private String calculatedAmount;
        private String exchangeRateUpdateDateTime;
    }
}
