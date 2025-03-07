package com.pitchain.dto.req;

import com.pitchain.entity.Company;

public record CreateCompanyReq(
        String email,
        String password,
        String passwordConfirmation
) {

    public Company createUnverifiedCompany(String encodedPassword) {
        return Company.createUnverifiedCompany(email, encodedPassword);
    }
}
