package com.pitchain.individual.application.res;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.constant.OauthProvider;
import com.pitchain.member.application.res.BaseMemberProfileRes;
import com.pitchain.individual.domain.Individual;
import com.pitchain.member.domain.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class IndividualProfileRes extends BaseMemberProfileRes {
    @NotNull
    private OauthProvider oauthProvider;

    public IndividualProfileRes(String profileImgKey, String name, String email, MemberRole memberRole, OauthProvider oauthProvider) {
        super(profileImgKey, name, email, memberRole);
        this.oauthProvider = oauthProvider;
    }

    public static IndividualProfileRes createRes(Member member, Individual individual) {
        return new IndividualProfileRes(member.getProfileImgKey(), member.getName(), member.getEmail(), member.getRole(), individual.getOauthProvider());
    }
}
