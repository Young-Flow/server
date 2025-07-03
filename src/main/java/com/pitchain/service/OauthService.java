package com.pitchain.service;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.dto.req.OauthLoginReq;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.entity.Individual;
import com.pitchain.entity.Member;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.oauth2.member.OauthMemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {
    private final TokenUtil tokenUtil;
    private final MemberService memberService;
    private final IndividualService individualService;
    private final RequestOauthInfoService requestOauthInfoService;

    @Transactional
    public LoginRes oauthLogin(OauthLoginReq req) {
        OauthMemberInfo memberInfo = requestOauthInfoService.request(
                req.getOauthProvider().getOauthParams(req.getCode())
        );

        Individual individual = saveIndividualIfNotExists(memberInfo);

        Long memberId = individual.getMember().getId();

        return issueTokens(memberId);
    }

    private Individual saveIndividualIfNotExists(OauthMemberInfo memberInfo) {
        return individualService
                .findByOauthProviderAndSocialId(memberInfo.getOauthProvider(), memberInfo.getSocialId())
                .orElseGet(() -> saveIndividual(memberInfo));
    }

    private Individual saveIndividual(OauthMemberInfo info) {
        Member member = memberService.saveIndividualMember(info.getEmail(), info.getNickname());
        return individualService.saveIndividual(member.getId(), info.getSocialId(), info.getOauthProvider());
    }

    private LoginRes issueTokens(Long memberId) {
        return LoginRes.createRes(
                tokenUtil.issueAccessToken(memberId, MemberRole.INDIVIDUAL),
                tokenUtil.issueRefreshToken(memberId, MemberRole.INDIVIDUAL)
        );
    }

}
