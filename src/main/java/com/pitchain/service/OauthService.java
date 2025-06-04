package com.pitchain.service;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.constant.OauthProvider;
import com.pitchain.dto.req.OauthLoginReq;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.entity.Individual;
import com.pitchain.entity.Member;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.oauth2.member.OauthMemberInfo;
import com.pitchain.oauth2.param.OauthParams;
import com.pitchain.repository.IndividualRepository;
import com.pitchain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class OauthService {
    private final RequestOauthInfoService requestOauthInfoService;
    private final TokenUtil tokenUtil;
    private final MemberService memberService;
    private final IndividualService individualService;

    public LoginRes getMemberByOauthLogin(OauthLoginReq req) {
        OauthMemberInfo oauthMemberInfo = requestOauthInfo(req);

        Optional<Individual> optionalIndividual = individualService.findByOauthProviderAndSocialId(
                oauthMemberInfo.getOauthProvider(), oauthMemberInfo.getSocialId()
        );

        if (optionalIndividual.isPresent()) {
            return handleMember(optionalIndividual.get());
        }

        return handleGuest(oauthMemberInfo);
    }

    private LoginRes handleMember(Individual individual) {
        Member member = individual.getMember();
        return createLoginRes(member.getId());
    }

    private LoginRes handleGuest(OauthMemberInfo oauthMemberInfo) {
        Member member = memberService.saveIndividualMember(oauthMemberInfo.getEmail(), oauthMemberInfo.getNickname());

        individualService.saveIndividual(member.getId(), oauthMemberInfo.getSocialId(), oauthMemberInfo.getOauthProvider());
        return createLoginRes(member.getId());
    }

    private LoginRes createLoginRes(Long memberId) {
        String accessToken = tokenUtil.issueAccessToken(memberId, MemberRole.INDIVIDUAL);
        String refreshToken = tokenUtil.issueRefreshToken(memberId, MemberRole.INDIVIDUAL);
        return LoginRes.createRes(accessToken, refreshToken);
    }

    private OauthMemberInfo requestOauthInfo(OauthLoginReq req) {
        OauthParams oauthParam = createOauthParams(req);
        OauthMemberInfo oauthMemberInfo = requestOauthInfoService.request(oauthParam);
        return oauthMemberInfo;
    }

    private static OauthParams createOauthParams(OauthLoginReq req) {
        OauthProvider oauthProvider = req.getOauthProvider();
        OauthParams oauthParam = oauthProvider.getOauthParams(req.getCode());
        return oauthParam;
    }

}
