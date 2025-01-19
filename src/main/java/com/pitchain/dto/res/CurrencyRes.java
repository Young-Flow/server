package com.pitchain.dto.res;

public record CurrencyRes(
        String calculatedAmount,
        String exchangeRateUpdateDateTime
) {
    public static CurrencyRes createRes(String calculatedAmount, String exchangeRateUpdateDateTime) {
        return new CurrencyRes(calculatedAmount, exchangeRateUpdateDateTime);
    }
}
