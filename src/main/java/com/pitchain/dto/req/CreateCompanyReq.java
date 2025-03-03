package com.pitchain.dto.req;

import com.pitchain.entity.Company;

public record CreateCompanyReq(
        String email,
        String password
) {

    public Company createUnverifiedCompany() {
        return Company.createUnverifiedCompany(email, password);
    }
}
