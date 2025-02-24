package com.pitchain.oauth2.member;

import com.pitchain.common.constant.OauthProvider;

public interface OauthMemberInfo {
    String getSocialId();

    String getEmail();

    String getNickname();

    OauthProvider getOauthProvider();
}
