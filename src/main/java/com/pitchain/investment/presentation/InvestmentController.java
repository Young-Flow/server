package com.pitchain.investment.presentation;

import com.pitchain.common.annotation.RequiredRole;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.investment.presentation.req.InvestmentAddReq;
import com.pitchain.investment.application.res.InvestmentStatusRes;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.investment.application.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bms")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    @RequiredRole(MemberRole.INDIVIDUAL)
    @PostMapping("/{bmId}/investments")
    public void addInvestment(@PathVariable("bmId") Long bmId,
                              @AuthenticationPrincipal MemberDetails memberDetails,
                              @Valid @RequestBody InvestmentAddReq req) {
        long amount = req.getAmount();
        investmentService.addInvestment(bmId, memberDetails, amount);
    }

    @GetMapping("/{bmId}/investments")
    public InvestmentStatusRes getInvestmentStatus(@PathVariable("bmId") Long bmId) {
        InvestmentStatusRes investmentStatusRes = investmentService.getInvestmentStatus(bmId);
        return investmentStatusRes;
    }
}
