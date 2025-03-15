package com.pitchain.dto.res;

import com.pitchain.common.constant.MemberRole;
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

    public IndividualProfileRes(String profileImgURL, String name, String email, MemberRole memberRole, OauthProvider oauthProvider) {
        super(profileImgURL, name, email, memberRole);
        this.oauthProvider = oauthProvider;
    }

    public static IndividualProfileRes createRes(Member member, Individual individual, String profileImgURL) {
        return new IndividualProfileRes(profileImgURL, member.getName(), member.getEmail(), member.getRole(), individual.getOauthProvider());
    }
}
