package com.pitchain.company.presentation.req;

import com.pitchain.company.domain.Company;
import com.pitchain.member.domain.Member;
import jakarta.validation.constraints.NotBlank;

public record CompanyCreateReq(
        @NotBlank(message = "이메일은 필수입니다.")
        String email,
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,
        @NotBlank(message = "비밀번호 확인 값은 필수입니다.")
        String passwordConfirmation
) {

    public Company createUnverifiedCompany(Member member, String encodedPassword) {
        return Company.createUnverifiedCompany(member, encodedPassword);
    }
}
