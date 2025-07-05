package com.pitchain.sp.presentation.req;

import jakarta.validation.constraints.NotNull;

public record SpCreateReq(
        @NotNull(message = "이름은 필수입니다.")
        String name
) {
}
