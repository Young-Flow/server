package com.pitchain.oauth.application.handler;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.oauth.application.memberinfo.OauthMemberInfo;
import com.pitchain.oauth.presentation.param.OauthParams;

public interface OauthHandler {
    OauthProvider oauthProvider();

    String getOauthLoginToken(OauthParams oauthParams);

    OauthMemberInfo getMemberInfo(String accessToken);

}
