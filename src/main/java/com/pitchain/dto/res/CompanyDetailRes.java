package com.pitchain.dto.res;

import com.pitchain.entity.Company;
import lombok.Builder;

@Builder
public record CompanyDetailRes(
        String logoImgKey,
        String name,
        String address
) {
    public static CompanyDetailRes createRes(Company company) {
        return CompanyDetailRes.builder()
                .logoImgKey(company.getLogoImgKey())
                .name(company.getName())
                .address(company.getAddress())
                .build();
    }
}
