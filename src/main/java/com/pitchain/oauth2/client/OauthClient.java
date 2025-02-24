package com.pitchain.oauth2.client;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.oauth2.member.OauthMemberInfo;
import com.pitchain.oauth2.param.OauthParams;

public interface OauthClient {
    OauthProvider oauthProvider();

    String getOauthLoginToken(OauthParams oauthParams);

    OauthMemberInfo getMemberInfo(String accessToken);

}
