package com.pitchain.dto.res;

import com.pitchain.common.constant.MemberRole;
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

    public CompanyProfileRes(String profileImgURL, String name, String email, MemberRole memberRole, String address, boolean isVerified) {
        super(profileImgURL, name, email, memberRole);
        this.address = address;
        this.isVerified = isVerified;
    }

    public static CompanyProfileRes createRes(Member member, Company company, String profileImgURL) {
        return new CompanyProfileRes(profileImgURL, member.getName(), member.getEmail(), member.getRole(), company.getAddress(), company.IsVerified());
    }
}
