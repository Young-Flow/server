package com.pitchain.dto.res;

import com.pitchain.dto.InvestmentStatusDto;
import com.pitchain.entity.Bm;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record InvestmentStatusRes(
        long raisedAmount,
        int achievementRate,
        long investorNum,
        long minimumAmount,
        long maximumAmount,

        @NotNull
        Long valuationCap,
        double pricePerShare,
        @NotNull
        Integer maxIssuedShare,
        @NotNull
        Long goalInvestment,
        @NotNull
        LocalDate deadline
) {
    public static InvestmentStatusRes createRes(Bm bm, InvestmentStatusDto investmentStatusDto, int achievementRate) {
        return InvestmentStatusRes.builder()
                .raisedAmount(investmentStatusDto.getRaisedAmount())
                .achievementRate(achievementRate)
                .investorNum(investmentStatusDto.getInvestorNum())
                .minimumAmount(investmentStatusDto.getMinimumAmount())
                .maximumAmount(investmentStatusDto.getMaximumAmount())
                .valuationCap(bm.getValuationCap())
                .pricePerShare(bm.getPricePerShare())
                .maxIssuedShare(bm.getMaxIssuedShare())
                .goalInvestment(bm.getGoalInvestment())
                .deadline(bm.getDeadline())
                .build();
    }
}
