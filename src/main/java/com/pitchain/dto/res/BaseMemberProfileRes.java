package com.pitchain.dto.res;

import lombok.Getter;

@Getter
public abstract class BaseMemberProfileRes {
    private final String profileImgURL;
    private final String name;
    private final String email;

    protected BaseMemberProfileRes(String profileImgURL, String name, String email) {
        this.profileImgURL = profileImgURL;
        this.name = name;
        this.email = email;
    }
}
