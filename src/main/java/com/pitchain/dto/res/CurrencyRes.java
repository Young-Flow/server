package com.pitchain.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public record CurrencyRes(
        String calculatedAmount,
        String exchangeRateUpdateDateTime
) {
    public static CurrencyRes createRes(String calculatedAmount, String exchangeRateUpdateDateTime) {
        return new CurrencyRes(calculatedAmount, exchangeRateUpdateDateTime);
    }
}
