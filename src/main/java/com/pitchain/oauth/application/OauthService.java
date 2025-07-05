package com.pitchain.oauth.application;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.oauth.presentation.req.OauthLoginReq;
import com.pitchain.oauth.application.res.LoginRes;
import com.pitchain.individual.domain.Individual;
import com.pitchain.member.domain.Member;
import com.pitchain.common.redis.RedisTokenUtil;
import com.pitchain.individual.application.IndividualService;
import com.pitchain.member.application.MemberService;
import com.pitchain.oauth.application.client.memberinfo.OauthMemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {
    private final RedisTokenUtil redisTokenUtil;
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
                redisTokenUtil.issueAccessToken(memberId, MemberRole.INDIVIDUAL),
                redisTokenUtil.issueRefreshToken(memberId, MemberRole.INDIVIDUAL)
        );
    }

}
