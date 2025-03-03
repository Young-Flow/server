package com.pitchain.service;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.dto.req.OauthLoginReq;
import com.pitchain.dto.res.OauthLoginRes;
import com.pitchain.entity.Member;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.oauth2.member.OauthMemberInfo;
import com.pitchain.oauth2.param.OauthParams;
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
    private final MemberRepository memberRepository;

    public OauthLoginRes getMemberByOauthLogin(OauthLoginReq req) {
        OauthParams oauthParam = createOauthParams(req);

        OauthMemberInfo oauthMemberInfo = requestOauthInfoService.request(oauthParam);
        Optional<Member> byOauthProviderAndSocialId = memberRepository.findByOauthProviderAndSocialId(oauthMemberInfo.getOauthProvider(), oauthMemberInfo.getSocialId());

        Member member = byOauthProviderAndSocialId.orElseGet(() -> memberRepository.save(new Member(oauthMemberInfo)));

        String accessToken = tokenUtil.issueAccessToken(member.getId());
        String refreshToken = tokenUtil.issueRefreshToken(member.getId());

        return OauthLoginRes.createRes(accessToken, refreshToken);
    }

    private static OauthParams createOauthParams(OauthLoginReq req) {
        OauthProvider oauthProvider = req.getOauthProvider();
        OauthParams oauthParam = oauthProvider.getOauthParams(req.getCode(), req.getState());
        return oauthParam;
    }

}
