package com.pitchain.dto.res;

import com.pitchain.common.annotation.S3Url;
import com.pitchain.common.constant.MemberRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class BaseMemberProfileRes {
    @NotEmpty
    @S3Url
    private final String profileImgURL;
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String email;
    @NotNull
    private final MemberRole memberRole;

    protected BaseMemberProfileRes(String profileImgKey, String name, String email, MemberRole memberRole) {
        this.profileImgURL = profileImgKey;  //추후에 JSON 직렬화 처리됨
        this.name = name;
        this.email = email;
        this.memberRole = memberRole;
    }
}
