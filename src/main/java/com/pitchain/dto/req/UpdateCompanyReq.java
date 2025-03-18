package com.pitchain.dto.req;

import com.pitchain.common.constant.MemberRole;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class UpdateCompanyReq extends BaseUpdateMemberReq {
    @Nullable
    private final String address;

    public UpdateCompanyReq(String address, MemberRole memberRole) {
        super(memberRole);
        this.address = address;
    }
}
