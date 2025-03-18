package com.pitchain.dto.req;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInvestmentReq {
    @Positive(message = "투자 금액은 양수여야 합니다.")
    private long amount;
}
