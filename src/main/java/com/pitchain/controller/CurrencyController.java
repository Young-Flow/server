package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.service.CurrencyService;
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
        return CustomApiResponse.onSuccess(currencyService.calculateExchangeRate(memberId, amount));
    }
}
