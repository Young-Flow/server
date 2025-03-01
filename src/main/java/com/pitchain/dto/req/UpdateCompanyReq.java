package com.pitchain.dto.req;

import com.pitchain.entity.Company;
import com.pitchain.entity.Member;

public record UpdateCompanyReq(
        String name,
        String address
) {

    public Company updateCompany(String logoImgKey, Member member) {
        return new Company(name, address, logoImgKey, member);
    }
}
