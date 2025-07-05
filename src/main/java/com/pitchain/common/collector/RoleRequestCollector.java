package com.pitchain.common.collector;

import com.pitchain.common.annotation.RequiredRole;
import com.pitchain.common.constant.MemberRole;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class RoleRequestCollector {
    private final Map<MemberRole, Map<HttpMethod, Set<String>>> roleUriMap = new HashMap<>();

    public RoleRequestCollector(ApplicationContext applicationContext) {
        for (MemberRole role : MemberRole.values()) {
            roleUriMap.put(role, new HashMap<>());
        }

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            collectRoleUris(entry, roleUriMap);
        }
        System.out.println();

    }

    private void collectRoleUris(Map.Entry<RequestMappingInfo, HandlerMethod> entry, Map<MemberRole, Map<HttpMethod, Set<String>>> roleUriMap) {
        HandlerMethod handlerMethod = entry.getValue();
        RequiredRole requiredRole = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), RequiredRole.class);

        if (requiredRole != null) {
            MemberRole memberRole = requiredRole.value();
            Map<HttpMethod, Set<String>> uriMap = roleUriMap.get(memberRole);

            RequestMappingInfo info = entry.getKey();

            for (RequestMethod requestMethod : info.getMethodsCondition().getMethods()) {
                HttpMethod httpMethod = requestMethod.asHttpMethod();
                Set<String> uris = uriMap.computeIfAbsent(httpMethod, k -> new HashSet<>());

                for (PathPattern pathPattern : info.getPathPatternsCondition().getPatterns()) {
                    String uri = pathPattern.getPatternString();
                    uris.add(convertToAntPatternString(uri));
                }
            }
        }
    }

    private static String convertToAntPatternString(String uri) {
        return uri.replaceAll("\\{[^/]+\\}", "*");
    }

    public Map<MemberRole, Map<HttpMethod, Set<String>>> getRoleUriMap() {
        return roleUriMap;
    }
}
