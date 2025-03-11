package com.pitchain.dto.req;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MemberRole;
import lombok.Getter;

@Getter
public class UpdateCompanyReq extends BaseUpdateMemberReq {
    private final String address;

    public UpdateCompanyReq(String email, String name, Country country, String address, MemberRole memberRole) {
        super(memberRole, email, name, country);
        this.address = address;
    }
}
