package com.pitchain.oauth2.param;

import com.pitchain.common.constant.OauthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@AllArgsConstructor
public class NaverParams implements OauthParams {
    private String authorizationCode;
    private String state;
    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.NAVER;
    }

    @Override
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public String getState() {
        return state;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("sate", state);
        return body;
    }
}
