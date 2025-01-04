package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.InvestmentDto;
import com.pitchain.dto.res.FundraisingRes;
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
    public CustomApiResponse<InvestmentDto.ResponseDto> addInvestment(@PathVariable("bmId") Long bmId,
                                                                      @AuthenticationPrincipal Long memberId,
                                                                      @Valid @RequestBody InvestmentDto.RequestDto dto) {

        long amount = dto.getAmount();
        Long investmentId = investmentService.addInvestment(bmId, memberId, amount);

        InvestmentDto.ResponseDto result = new InvestmentDto.ResponseDto();
        result.setInvestmentId(investmentId);

        return CustomApiResponse.onSuccess(result);
    }

    @GetMapping("/{bmId}/fundraising-status")
    public CustomApiResponse getFundraisingStatus(@PathVariable("bmId") Long bmId) {
        FundraisingRes fundraisingStatus = investmentService.getFundraisingStatus(bmId);
        return CustomApiResponse.onSuccess(fundraisingStatus);
    }
}
