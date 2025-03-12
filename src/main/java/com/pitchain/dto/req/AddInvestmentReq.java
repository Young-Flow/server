package com.pitchain.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public class AddInvestmentReq {
    @Positive
    private long amount;
}
