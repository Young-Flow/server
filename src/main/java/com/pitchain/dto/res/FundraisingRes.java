package com.pitchain.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundraisingRes {
    private long raisedAmount;
    private int achievementRate;
    private int investorNum;
    private long minimumAmount;
    private long maximumAmount;
}
