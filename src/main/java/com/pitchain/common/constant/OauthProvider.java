package com.pitchain.common.constant;

import com.pitchain.dto.req.OauthLoginReq;
import com.pitchain.oauth2.param.GoogleParams;
import com.pitchain.oauth2.param.KakaoParams;
import com.pitchain.oauth2.param.NaverParams;
import com.pitchain.oauth2.param.OauthParams;

public enum OauthProvider {
    KAKAO, GOOGLE, NAVER;

    public OauthParams getOauthParams(OauthLoginReq req) {
        String code = req.getCode();
        String state = req.getState();

        return switch (this) {
            case KAKAO -> new KakaoParams(code);
            case GOOGLE -> new GoogleParams(code);
            case NAVER -> {
                assert state != null : "state should not be null";
                yield new NaverParams(code, state);
            }
        };
    }
}
