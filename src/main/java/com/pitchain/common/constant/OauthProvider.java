package com.pitchain.common.constant;

import com.pitchain.oauth.presentation.param.GoogleParams;
import com.pitchain.oauth.presentation.param.KakaoParams;
import com.pitchain.oauth.presentation.param.NaverParams;
import com.pitchain.oauth.presentation.param.OauthParams;

public enum OauthProvider {
    KAKAO, GOOGLE, NAVER;

    public OauthParams getOauthParams(String code) {
        return switch (this) {
            case KAKAO -> new KakaoParams(code);
            case GOOGLE -> new GoogleParams(code);
            case NAVER -> new NaverParams(code);
        };
    }
}
