package com.pitchain.dto.req;

import com.pitchain.entity.Company;
import com.pitchain.entity.Member;

public record CreateCompanyReq(
        String email,
        String password,
        String passwordConfirmation
) {

    public Company createUnverifiedCompany(Member member, String encodedPassword) {
        return Company.createUnverifiedCompany(member, encodedPassword);
    }
}
