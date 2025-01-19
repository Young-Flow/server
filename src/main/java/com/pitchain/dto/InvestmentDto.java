package com.pitchain.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestmentDto {
    @Positive
    private long amount;
}
