package com.pitchain.controller;

import com.pitchain.dto.req.InvestmentAddReq;
import com.pitchain.dto.res.InvestmentStatusRes;
import com.pitchain.jwt.MemberDetails;
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
    public void addInvestment(@PathVariable("bmId") Long bmId,
                              @AuthenticationPrincipal MemberDetails memberDetails,
                              @Valid @RequestBody InvestmentAddReq req) {
        long amount = req.getAmount();
        investmentService.addInvestment(bmId, memberDetails, amount);
    }

    @GetMapping("/{bmId}/investment")
    public InvestmentStatusRes getInvestmentStatus(@PathVariable("bmId") Long bmId) {
        InvestmentStatusRes investmentStatusRes = investmentService.getInvestmentStatus(bmId);
        return investmentStatusRes;
    }
}
