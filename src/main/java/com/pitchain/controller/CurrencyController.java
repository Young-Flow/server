package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.CurrencyDto;
import com.pitchain.service.CurrencyService;
import com.pitchain.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/currency")
    public CustomApiResponse calculateExchangeRate(@AuthenticationPrincipal Long memberId,
                                                   @RequestParam Integer amount) {
        String calculatedAmount = currencyService.calculateExchangeRate(memberId, amount);
        String exchangeRateUpdateDateTime = currencyService.getExchangeRateUpdateDateTime();

        CurrencyDto.ResponseDto result = new CurrencyDto.ResponseDto();
        result.setCalculatedAmount(calculatedAmount);
        result.setExchangeRateUpdateDateTime(exchangeRateUpdateDateTime);

        return CustomApiResponse.onSuccess(result);
    }
}
