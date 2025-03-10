package com.pitchain.dto.req;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MemberRole;

public class UpdateIndividualReq extends BaseUpdateMemberReq {
    public UpdateIndividualReq(String email, String name, Country country, MemberRole memberRole) {
        super(memberRole, email, name, country);
    }
}
