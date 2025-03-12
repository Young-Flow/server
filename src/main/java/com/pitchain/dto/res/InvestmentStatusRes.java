package com.pitchain.dto.res;

import com.pitchain.dto.InvestmentStatusDto;
import com.pitchain.entity.Bm;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public record InvestmentStatusRes(
        long raisedAmount,
        int achievementRate,
        long investorNum,
        long minimumAmount,
        long maximumAmount,

        Long valuationCap,
        double pricePerShare,
        Integer maxIssuedShare,
        Long goalInvestment,
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
