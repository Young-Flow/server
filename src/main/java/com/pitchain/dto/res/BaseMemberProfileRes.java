package com.pitchain.dto.res;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.converter.S3UrlSerializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class BaseMemberProfileRes {
    @NotEmpty
    @JsonSerialize(using = S3UrlSerializer.class)
    private final String profileImgURL;
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String email;
    @NotNull
    private final MemberRole memberRole;

    protected BaseMemberProfileRes(String profileImgURL, String name, String email, MemberRole memberRole) {
        this.profileImgURL = profileImgURL;
        this.name = name;
        this.email = email;
        this.memberRole = memberRole;
    }
}
