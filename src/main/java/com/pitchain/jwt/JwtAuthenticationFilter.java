package com.pitchain.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.constant.TokenType;
import com.pitchain.common.exception.GeneralException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;

    public static final String[] whitelist = {
            "/oauth2/**",
            "/resources/**", "/favicon.ico", // resource
            "/swagger-ui/**", "/api-docs/**", "/v3/api-docs**", "/v3/api-docs/**", // swagger
            "/health-check", // health check
            "/dev/**", // 개발용,
            "/members/tokens", // 공통 유저
            "/companies", "/companies/login", "/companies/emails"// 회사
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PatternMatchUtils.simpleMatch(whitelist, request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenUtil.extractToken(request, TokenType.ACCESS_TOKEN);

        if (token == null)
            throw new GeneralException(ErrorStatus.TOKEN_MISSING);

        DecodedJWT decodedJWT = tokenUtil.decodedJWT(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();

        MemberRole memberRole = MemberRole.toEnum(role);
        MemberDetails memberDetails = new MemberDetails(id, memberRole);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberRole.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        doFilter(request, response, filterChain);
    }
}
