package com.pitchain.common.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.constant.TokenType;
import com.pitchain.common.redis.RedisTokenUtil;
import com.pitchain.common.security.MemberClaims;
import com.pitchain.common.security.MemberDetails;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final RedisTokenUtil redisTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            verifyAccessToken(request);
        } catch (JWTVerificationException e1) {
            try {
                verifyRefreshToken(request, response);
            } catch (JWTVerificationException e2) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void verifyAccessToken(HttpServletRequest request) {
        String accessToken = redisTokenUtil.extractToken(request, TokenType.ACCESS_TOKEN);
        MemberClaims claim = redisTokenUtil.getClaim(accessToken);
        setAuthentication(claim);
    }

    private void verifyRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = redisTokenUtil.extractToken(request, TokenType.REFRESH_TOKEN);
        MemberClaims claims = redisTokenUtil.getClaim(refreshToken);
        redisTokenUtil.reissueToken(response, claims);
        setAuthentication(claims);
    }

    private void setAuthentication(MemberClaims claims) {
        Long id = claims.getId();
        MemberRole memberRole = claims.getMemberRole();

        MemberDetails memberDetails = new MemberDetails(id, memberRole);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberRole.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
