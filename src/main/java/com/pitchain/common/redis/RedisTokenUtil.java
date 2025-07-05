package com.pitchain.common.redis;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.constant.TokenType;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.common.security.MemberClaims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class RedisTokenUtil {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";

    public String issueAccessToken(Long memberId, MemberRole memberRole) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withClaim("id", memberId)
                .withClaim("role", memberRole.name())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    // todo 프로토타입 시연을 위한 임시 메소드
    public String issueAccessTokenWithoutExpiration(Long memberId, MemberRole memberRole) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withClaim("id", memberId)
                .withClaim("role", memberRole.name())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10800000))  //3시간
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String issueRefreshToken(Long memberId, MemberRole memberRole) {
        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withClaim("id", memberId)
                .withClaim("role", memberRole.name())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
        return refreshToken;
    }

    public String issueRefreshTokenWithoutExpiration(Long memberId, MemberRole memberRole) {
        String refreshToken = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withClaim("id", memberId)
                .withClaim("role", memberRole.name())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10800000))  //3시간
                .sign(Algorithm.HMAC512(secretKey));
        return refreshToken;
    }

    public String reissueAccessToken(String refreshToken) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(refreshToken);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(e.getMessage());
        }

        Long memberId = decodedJWT.getClaim("id").asLong();
        MemberRole memberRole = MemberRole.valueOf(decodedJWT.getClaim("role").asString());

        return issueAccessToken(memberId, memberRole);
    }

    public String extractToken(HttpServletRequest request, TokenType tokenType) {
        Optional<String> requestToken = switch (tokenType) {
            case ACCESS_TOKEN -> Optional.ofNullable(request.getHeader(accessHeader))
                    .filter(token -> token.startsWith(BEARER))
                    .map(token -> token.substring(7));
            case REFRESH_TOKEN -> Optional.ofNullable(request.getHeader(refreshHeader))
                    .filter(token -> token.startsWith(BEARER))
                    .map(token -> token.substring(7));
            default -> throw new IllegalStateException("Unexpected value: " + tokenType);
        };

        return requestToken.orElse(null);
    }

    public DecodedJWT decodedJWT(String accessToken) {
        try {
            return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken);
        } catch (TokenExpiredException e) {
            log.debug("AccessToken is expired: ${}", accessToken);
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        } catch (JWTVerificationException e) {
            log.debug("AccessToken verification is failed because " + e.getMessage());
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }
    }

    public MemberClaims getClaim(String refreshToken) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(refreshToken);
            Long id = decodedJWT.getClaim("id").asLong();
            MemberRole memberRole = MemberRole.valueOf(decodedJWT.getClaim("role").asString());
            return new MemberClaims(id, memberRole);
        } catch (JWTVerificationException e) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }
    }
}
