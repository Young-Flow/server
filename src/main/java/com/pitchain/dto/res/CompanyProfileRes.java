package com.pitchain.dto.res;

import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public class CompanyProfileRes extends BaseMemberProfileRes {
    private final String address;
    private final boolean isVerified;

    @Builder
    public CompanyProfileRes(String profileImgURL, String name, String email, String address, boolean isVerified) {
        super(profileImgURL, name, email);
        this.address = address;
        this.isVerified = isVerified;
    }

    public static CompanyProfileRes createRes(Member member, Company company, String profileImgURL) {
        return CompanyProfileRes.builder()
                .email(member.getEmail())
                .profileImgURL(profileImgURL)
                .name(member.getName())
                .address(company.getAddress())
                .isVerified(company.getIsVerified())
                .build();
    }
}
