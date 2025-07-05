package com.pitchain.oauth.application.client;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.oauth.application.client.memberinfo.OauthMemberInfo;
import com.pitchain.oauth.presentation.param.OauthParams;

public interface OauthClient {
    OauthProvider oauthProvider();

    String getOauthLoginToken(OauthParams oauthParams);

    OauthMemberInfo getMemberInfo(String accessToken);

}
