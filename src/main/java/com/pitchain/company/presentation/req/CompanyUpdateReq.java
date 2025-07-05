package com.pitchain.company.presentation.req;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.member.presentation.req.BaseMemberUpdateReq;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class CompanyUpdateReq extends BaseMemberUpdateReq {
    @Nullable
    private final String address;

    public CompanyUpdateReq(String address, MemberRole memberRole) {
        super(memberRole);
        this.address = address;
    }
}
