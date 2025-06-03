package com.pitchain.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class MySpHistoryCreateReq {
    @NotNull(message = "BM ID는 필수입니다.")
    private Long bmId;

    @Schema(description = "사용자가 해당 BM의 SP를 시청한 시간(ms 단위)")
    @Positive(message = "시청 시간은 양수여야 합니다.")
    private int viewTime;
}
