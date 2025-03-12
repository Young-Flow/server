package com.pitchain.dto.req;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public class UpdateIndividualReq extends BaseUpdateMemberReq {
    public UpdateIndividualReq(String email, String name, Country country, MemberRole memberRole) {
        super(memberRole, email, name, country);
    }
}
