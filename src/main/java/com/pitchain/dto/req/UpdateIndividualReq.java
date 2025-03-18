package com.pitchain.dto.req;

import com.pitchain.common.constant.MemberRole;
import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class UpdateIndividualReq extends BaseUpdateMemberReq {
    @Nullable
    private final String name;
    public UpdateIndividualReq(String name, MemberRole memberRole) {
        super(memberRole);
        this.name = name;
    }
}
