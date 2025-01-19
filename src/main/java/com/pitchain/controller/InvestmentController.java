package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.InvestmentDto;
import com.pitchain.service.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bms")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    @PostMapping("/{bmId}/investments")
    public CustomApiResponse<InvestmentDto> addInvestment(@PathVariable("bmId") Long bmId,
                                                          @AuthenticationPrincipal Long memberId,
                                                          @Valid @RequestBody InvestmentDto dto) {
        long amount = dto.getAmount();
        investmentService.addInvestment(bmId, memberId, amount);
        return CustomApiResponse.onSuccess();
    }

    @GetMapping("/{bmId}/investment")
    public CustomApiResponse getInvestmentStatus(@PathVariable("bmId") Long bmId) {
        return CustomApiResponse.onSuccess(investmentService.getInvestmentStatus(bmId));
    }
}
