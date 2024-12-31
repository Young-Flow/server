package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.CurrencyDto;
import com.pitchain.service.CurrencyService;
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

    @GetMapping("/currency")
    public CustomApiResponse getCurrency(@AuthenticationPrincipal Long memberId,
                                         @Valid @RequestBody CurrencyDto.RequestDto dto) {
        String calculatedAmount = currencyService.calculateExchangeRate(memberId, dto);

        CurrencyDto.ResponseDto result = new CurrencyDto.ResponseDto();
        result.setCalculatedAmount(calculatedAmount);

        return CustomApiResponse.onSuccess(result);
    }
}
