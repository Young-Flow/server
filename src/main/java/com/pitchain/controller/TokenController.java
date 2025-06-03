package com.pitchain.controller;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.jwt.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "JWT 발급")
@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenUtil tokenUtil;

    @Operation(summary = "Access Token 발급 / 개발용 / 3시간 이후 만료")
    @GetMapping("/dev/access-token")
    public String issueAccessTokenWithoutExpiration(@RequestParam Long id, @RequestParam MemberRole memberRole) {
        return tokenUtil.issueAccessTokenWithoutExpiration(id, memberRole);
    }

    @Operation(summary = "Refresh Token 발급 / 개발용 / 3시간 이후 만료")
    @GetMapping("/dev/refresh-token")
    public String token(@RequestParam Long id, @RequestParam MemberRole memberRole) {
        return tokenUtil.issueRefreshTokenWithoutExpiration(id, memberRole);
    }
}
