package com.pitchain.oauth2.client;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.OauthProvider;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.oauth2.member.GoogleMemberInfo;
import com.pitchain.oauth2.member.OauthMemberInfo;
import com.pitchain.oauth2.param.OauthParams;
import com.pitchain.oauth2.token.GoogleToken;
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
public class GoogleClient implements OauthClient {
    @Value("${oauth.google.token_url}")
    private String token_url;
    @Value("${oauth.google.user_url}")
    private String user_url;
    @Value("${oauth.google.grant_type}")
    private String grant_type;
    @Value("${oauth.google.client_id}")
    private String client_id;
    @Value("${oauth.google.client_secret}")
    private String client_secret;
    @Value("${oauth.google.redirect_uri}")
    private String redirect_uri;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.GOOGLE;
    }

    @Override
    public String getOauthLoginToken(OauthParams oauthParams) {
        String url = token_url;
        log.debug("Authorization code: " + oauthParams.getAuthorizationCode());

        RestTemplate rt = new RestTemplate();
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 바디 생성
        MultiValueMap<String, String> body = oauthParams.makeBody();
        body.add("grant_type", grant_type);
        body.add("client_id", client_id);
        body.add("client_secret", client_secret);
        body.add("redirect_uri", redirect_uri);

        // 헤더 + 바디 결합
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        log.debug("Current httpEntity state: " + tokenRequest);

        // 토큰 수신
        GoogleToken googleToken = rt.postForObject(url, tokenRequest, GoogleToken.class);
        log.debug("accessToken: " + googleToken);

        if (googleToken == null) {
            log.error("google token을 정상적으로 가져오지 못했습니다.");
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

        return googleToken.getAccess_token();
    }

    @Override
    public OauthMemberInfo getMemberInfo(String accessToken) {
        String url = user_url;
        log.debug("Received token: " + accessToken);

        // 요청 객체 생성
        RestTemplate rt = new RestTemplate();

        // 헤더 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        // Google 사용자 정보 요청 시 바디는 필요 없음
        HttpEntity<String> memberInfoRequest = new HttpEntity<>(httpHeaders);

        return rt.postForObject(url, memberInfoRequest, GoogleMemberInfo.class);
    }
}
