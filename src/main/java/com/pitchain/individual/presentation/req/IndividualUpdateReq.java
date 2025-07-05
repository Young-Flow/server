package com.pitchain.individual.presentation.req;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.member.presentation.req.BaseMemberUpdateReq;
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
