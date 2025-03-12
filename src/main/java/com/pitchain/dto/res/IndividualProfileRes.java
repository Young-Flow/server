package com.pitchain.dto.res;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.entity.Individual;
import com.pitchain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public class IndividualProfileRes extends BaseMemberProfileRes {
    private OauthProvider oauthProvider;

    @Builder
    public IndividualProfileRes(String profileImgURL, String name, String email, OauthProvider oauthProvider) {
        super(profileImgURL, name, email);
        this.oauthProvider = oauthProvider;
    }

    public static IndividualProfileRes createRes(Member member, Individual individual, String profileImgURL) {
        return IndividualProfileRes.builder()
                .profileImgURL(profileImgURL)
                .name(member.getName())
                .email(member.getEmail())
                .oauthProvider(individual.getOauthProvider())
                .build();
    }
}
