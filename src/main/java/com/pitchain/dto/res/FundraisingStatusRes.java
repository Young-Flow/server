package com.pitchain.dto.res;

import com.pitchain.dto.FundraisingStatusDto;
import lombok.Builder;

@Builder
public record FundraisingStatusRes(
        long raisedAmount,
        int achievementRate,
        long investorNum,
        long minimumAmount,
        long maximumAmount
) {
    public static FundraisingStatusRes createFundraisingRes(FundraisingStatusDto fundraisingStatusDto, int achievementRate) {
        return FundraisingStatusRes.builder()
                .raisedAmount(fundraisingStatusDto.getRaisedAmount())
                .achievementRate(achievementRate)
                .investorNum(fundraisingStatusDto.getInvestorNum())
                .minimumAmount(fundraisingStatusDto.getMinimumAmount())
                .maximumAmount(fundraisingStatusDto.getMaximumAmount())
                .build();
    }
}
