package com.pitchain.dto.req;

import jakarta.validation.constraints.NotNull;

public record SpCreateReq(
        @NotNull(message = "BM ID는 필수입니다.")
        Long bmId,
        @NotNull(message = "이름은 필수입니다.")
        String name
) {
}
