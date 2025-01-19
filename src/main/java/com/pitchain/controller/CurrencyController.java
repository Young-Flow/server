package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.res.CurrencyRes;
import com.pitchain.service.CurrencyService;
import com.pitchain.service.ExchangeRateService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CurrencyController {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/currency")
    public CustomApiResponse calculateExchangeRate(@AuthenticationPrincipal Long memberId,
                                                   @RequestParam @Positive long amount) {
        String calculatedAmount = currencyService.calculateExchangeRate(memberId, amount);
        String exchangeRateUpdateDateTime = currencyService.getExchangeRateUpdateDateTime();

        CurrencyRes currencyRes = CurrencyRes.createRes(calculatedAmount, exchangeRateUpdateDateTime);
        return CustomApiResponse.onSuccess(currencyRes);
    }

    @GetMapping("/currency-test")
    public CustomApiResponse test() {
        exchangeRateService.updateExchangeRateMap();
        return CustomApiResponse.onSuccess();
    }
}
