package com.pitchain.oauth.application.handler;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.oauth.application.memberinfo.OauthMemberInfo;
import com.pitchain.oauth.presentation.param.OauthParams;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OauthHandlerDispatcher {
    private final Map<OauthProvider, OauthHandler> handlerMap;

    public OauthHandlerDispatcher(List<OauthHandler> handlerMap) {
        this.handlerMap = handlerMap.stream().collect(
                Collectors.toUnmodifiableMap(OauthHandler::oauthProvider, Function.identity()));
    }

    public OauthMemberInfo handleRequest(OauthParams oauthParams) {
        OauthHandler handler = handlerMap.get(oauthParams.oauthProvider());
        String accessToken = handler.getOauthLoginToken(oauthParams);
        return handler.getMemberInfo(accessToken);
    }
}
