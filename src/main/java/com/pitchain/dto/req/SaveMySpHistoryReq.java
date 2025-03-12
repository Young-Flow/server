package com.pitchain.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public class SaveMySpHistoryReq {
    @NotNull
    private Long bmId;

    @Schema(description = "사용자가 해당 BM의 SP를 시청한 시간(ms 단위)")
    @Positive
    private int viewTime;
}
