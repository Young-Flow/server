package com.pitchain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDto {
    private String calculatedAmount;
    private String exchangeRateUpdateDateTime;
}
