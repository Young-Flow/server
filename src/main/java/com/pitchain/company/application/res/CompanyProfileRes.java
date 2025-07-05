package com.pitchain.company.application.res;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.member.application.res.BaseMemberProfileRes;
import com.pitchain.company.domain.Company;
import com.pitchain.member.domain.Member;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CompanyProfileRes extends BaseMemberProfileRes {
    @NotEmpty
    private final String address;
    @NotNull
    private final Boolean isVerified;

    public CompanyProfileRes(String profileImgKey, String name, String email, MemberRole memberRole, String address, Boolean isVerified) {
        super(profileImgKey, name, email, memberRole);
        this.address = address;
        this.isVerified = isVerified;
    }

    public static CompanyProfileRes createRes(Member member, Company company) {
        return new CompanyProfileRes(member.getProfileImgKey(), member.getName(), member.getEmail(), member.getRole(), company.getAddress(), company.IsVerified());
    }
}
