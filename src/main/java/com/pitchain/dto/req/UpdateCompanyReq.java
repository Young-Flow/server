package com.pitchain.dto.req;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public class UpdateCompanyReq extends BaseUpdateMemberReq {
    private final String address;

    public UpdateCompanyReq(String email, String name, Country country, String address, MemberRole memberRole) {
        super(memberRole, email, name, country);
        this.address = address;
    }
}
