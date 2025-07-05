package com.pitchain.company.presentation.req;

import jakarta.validation.constraints.NotBlank;

public record CompanyVerifyReq(
        @NotBlank(message = "회사 이름은 필수입니다.")
        String companyName) {
}
