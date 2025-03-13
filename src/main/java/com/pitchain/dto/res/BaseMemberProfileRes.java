package com.pitchain.dto.res;

import com.pitchain.common.constant.MemberRole;
import lombok.Getter;

@Getter
public abstract class BaseMemberProfileRes {
    private final String profileImgURL;
    private final String name;
    private final String email;
    private final MemberRole memberRole;

    protected BaseMemberProfileRes(String profileImgURL, String name, String email, MemberRole memberRole) {
        this.profileImgURL = profileImgURL;
        this.name = name;
        this.email = email;
        this.memberRole = memberRole;
    }
}
