package com.pitchain.dto.req;

import com.pitchain.common.constant.MemberRole;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class IndividualUpdateReq extends BaseMemberUpdateReq {
    @Nullable
    private final String name;
    public IndividualUpdateReq(String name, MemberRole memberRole) {
        super(memberRole);
        this.name = name;
    }
}
