package com.pitchain.dto.res;

import com.pitchain.dto.InvestmentStatusDto;
import lombok.Builder;

@Builder
public record InvestmentStatusRes(
        long raisedAmount,
        int achievementRate,
        long investorNum,
        long minimumAmount,
        long maximumAmount
) {
    public static InvestmentStatusRes createRes(InvestmentStatusDto investmentStatusDto, int achievementRate) {
        return InvestmentStatusRes.builder()
                .raisedAmount(investmentStatusDto.getRaisedAmount())
                .achievementRate(achievementRate)
                .investorNum(investmentStatusDto.getInvestorNum())
                .minimumAmount(investmentStatusDto.getMinimumAmount())
                .maximumAmount(investmentStatusDto.getMaximumAmount())
                .build();
    }
}
