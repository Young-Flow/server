package com.pitchain.dto.res;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.entity.Member;
import lombok.Builder;

@Builder
public record MemberDetailRes(
        String profileImgURL,
        String nickname,
        String email,
        OauthProvider oauthProvider
) {
    public static MemberDetailRes createRes(Member member, String profileImgURL) {
        return MemberDetailRes.builder()
                .profileImgURL(profileImgURL)
                .nickname(member.getNickname())
                .email(member.getEmail())
                .oauthProvider(member.getOauthProvider())
                .build();
    }
}
