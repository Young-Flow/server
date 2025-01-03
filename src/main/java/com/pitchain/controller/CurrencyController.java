package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.CurrencyDto;
import com.pitchain.service.CurrencyService;
import com.pitchain.service.ExchangeRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CurrencyController {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/currency")
    public CustomApiResponse calculateExchangeRate(@AuthenticationPrincipal Long memberId,
                                                   @Valid @RequestBody CurrencyDto.RequestDto dto) {
        long inputAmount = dto.getInputAmount();
        String calculatedAmount = currencyService.calculateExchangeRate(memberId, inputAmount);
        String exchangeRateUpdateDateTime = currencyService.getExchangeRateUpdateDateTime();

        CurrencyDto.ResponseDto result = new CurrencyDto.ResponseDto();
        result.setCalculatedAmount(calculatedAmount);
        result.setExchangeRateUpdateDateTime(exchangeRateUpdateDateTime);

        return CustomApiResponse.onSuccess(result);
    }
}
