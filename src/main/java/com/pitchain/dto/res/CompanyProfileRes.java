package com.pitchain.dto.res;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CompanyProfileRes extends BaseMemberProfileRes {
    @NotEmpty
    private final String address;
    @NotNull
    private final Boolean isVerified;

    public CompanyProfileRes(String profileImgURL, String name, String email, MemberRole memberRole, String address, Boolean isVerified) {
        super(profileImgURL, name, email, memberRole);
        this.address = address;
        this.isVerified = isVerified;
    }

    public static CompanyProfileRes createRes(Member member, Company company, String profileImgURL) {
        return new CompanyProfileRes(profileImgURL, member.getName(), member.getEmail(), member.getRole(), company.getAddress(), company.IsVerified());
    }
}
