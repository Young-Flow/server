package com.pitchain.dto.req;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInvestmentReq {
    @Positive
    private long amount;
}
