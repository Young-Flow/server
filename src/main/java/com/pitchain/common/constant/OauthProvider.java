package com.pitchain.common.constant;

import com.pitchain.oauth2.param.GoogleParams;
import com.pitchain.oauth2.param.KakaoParams;
import com.pitchain.oauth2.param.OauthParams;

public enum OauthProvider {
    KAKAO, GOOGLE;

    public OauthParams getOauthParams(String code) {
        return switch (this) {
            case KAKAO -> new KakaoParams(code);
            case GOOGLE -> new GoogleParams(code);
        };
    }
}
