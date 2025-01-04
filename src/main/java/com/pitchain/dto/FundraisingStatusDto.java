package com.pitchain.dto;

import lombok.Getter;

@Getter
public class FundraisingStatusDto {
    private Long raisedAmount;
    private Long investorNum;
    private Long minimumAmount;
    private Long maximumAmount;

    public FundraisingStatusDto(Long raisedAmount, Long investorNum, Long minimumAmount, Long maximumAmount) {
        this.raisedAmount = raisedAmount == null ? 0 : raisedAmount;
        this.investorNum = investorNum;
        this.minimumAmount = minimumAmount == null ? 0 : minimumAmount;
        this.maximumAmount = maximumAmount == null ? 0 : maximumAmount;
    }
}
