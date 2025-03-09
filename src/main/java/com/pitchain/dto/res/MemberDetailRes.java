package com.pitchain.dto.res;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.entity.Individual;
import com.pitchain.entity.Member;
import lombok.Builder;

@Builder
public record MemberDetailRes(
        String profileImgURL,
        String name,
        String email,
        OauthProvider oauthProvider
) {
    public static MemberDetailRes createRes(Member member, Individual individual, String profileImgURL) {
        return MemberDetailRes.builder()
                .profileImgURL(profileImgURL)
                .name(member.getName())
                .email(member.getEmail())
                .oauthProvider(individual.getOauthProvider())
                .build();
    }
}
