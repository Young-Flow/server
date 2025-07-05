package com.pitchain.oauth.application.memberinfo;

import com.pitchain.common.constant.OauthProvider;

public interface OauthMemberInfo {
    String getSocialId();

    String getEmail();

    String getNickname();

    OauthProvider getOauthProvider();
}
