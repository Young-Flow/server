package com.pitchain.dto.res;

import com.pitchain.entity.Company;
import lombok.Builder;

@Builder
public record CompanyDetailRes(
        String email,
        String logoImgKey,
        String name,
        String address,
        Boolean isVerified
) {
    public static CompanyDetailRes createRes(Company company) {
        return CompanyDetailRes.builder()
                .email(company.getEmail())
                .logoImgKey(company.getLogoImgKey())
                .name(company.getName())
                .address(company.getAddress())
                .isVerified(company.getIsVerified())
                .build();
    }
}
