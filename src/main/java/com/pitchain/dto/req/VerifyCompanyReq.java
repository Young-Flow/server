package com.pitchain.dto.req;

import jakarta.validation.constraints.NotBlank;

public record VerifyCompanyReq(
        @NotBlank(message = "회사 이름은 필수입니다.")
        String companyName) {
}
