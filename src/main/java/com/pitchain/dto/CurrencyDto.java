package com.pitchain.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

public class CurrencyDto {

    @Getter
    @Setter
    public static class RequestDto {

        @Positive
        private long inputAmount;  //원화 기준
    }

    @Getter
    @Setter
    public static class ResponseDto {

        private String calculatedAmount;  //사용자의 통화 기준
        private String exchangeRateUpdateDateTime;
    }
}
