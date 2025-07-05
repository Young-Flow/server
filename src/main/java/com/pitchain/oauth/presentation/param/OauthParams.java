package com.pitchain.oauth.presentation.param;

import com.pitchain.common.constant.OauthProvider;
import org.springframework.util.MultiValueMap;

public interface OauthParams {
    OauthProvider oauthProvider();

    String getAuthorizationCode();

    MultiValueMap<String, String> makeBody();
}
