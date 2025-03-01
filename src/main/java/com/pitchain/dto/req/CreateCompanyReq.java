package com.pitchain.dto.req;

import com.pitchain.entity.Company;
import com.pitchain.entity.Member;

public record CreateCompanyReq(
        String name,
        String address
) {

    public Company createCompany(String logoImgKey, Member member) {
        return new Company(name, address, logoImgKey, member);
    }
}
