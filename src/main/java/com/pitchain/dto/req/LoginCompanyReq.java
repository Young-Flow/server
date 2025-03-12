package com.pitchain.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public record LoginCompanyReq(
        String email,
        String password
) {
}
