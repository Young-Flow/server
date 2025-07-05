package com.pitchain.oauth.application.handler;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.OauthProvider;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.oauth.application.memberinfo.NaverMemberInfo;
import com.pitchain.oauth.application.memberinfo.OauthMemberInfo;
import com.pitchain.oauth.presentation.param.OauthParams;
import com.pitchain.oauth.presentation.token.NaverToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class NaverOauthHandler implements OauthHandler {
    @Value("${oauth.naver.token_url}")
    private String token_url;
    @Value("${oauth.naver.user_url}")
    private String user_url;
    @Value("${oauth.naver.grant_type}")
    private String grant_type;
    @Value("${oauth.naver.client_id}")
    private String client_id;
    @Value("${oauth.naver.client_secret}")
    private String client_secret;
    @Value("${oauth.naver.redirect_uri}")
    private String redirect_uri;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.NAVER;
    }

    @Override
    public String getOauthLoginToken(OauthParams oauthParams) {
        String url = token_url;
        log.debug("Authorization code: " + oauthParams.getAuthorizationCode());

        RestTemplate rt = new RestTemplate();
        //헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //바디 생성
        MultiValueMap<String, String> body = oauthParams.makeBody();
        body.add("grant_type", grant_type);
        body.add("client_id", client_id);
        body.add("client_secret", client_secret);
        body.add("redirect_uri", redirect_uri);

        //헤더 + 바디
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        log.debug("Current httpEntity state: " + tokenRequest);

        //소셜토큰 수신
        NaverToken naverToken = rt.postForObject(url, tokenRequest, NaverToken.class);
        log.debug("accessToken: " + naverToken);

        if (naverToken == null) {
            log.error("naver token을 정상적으로 가져오지 못했습니다.");
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        return naverToken.getAccess_token();
    }

    @Override
    public OauthMemberInfo getMemberInfo(String accessToken) {
        String url = user_url;

        // 요청 객체 생성
        RestTemplate rt = new RestTemplate();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + accessToken);

        // Naver 사용자 정보 요청 시 바디는 필요 없음
        HttpEntity<String> memberInfoRequest = new HttpEntity<>(headers);

        return rt.postForObject(url, memberInfoRequest, NaverMemberInfo.class);
    }
}
