package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
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

    @Operation(summary = "토큰 발급 / 개발용")
    @GetMapping("/dev-token")
    public CustomApiResponse<String> token(@RequestParam Long id, @RequestParam MemberRole memberRole) {
        return CustomApiResponse.onSuccess(tokenUtil.issueAccessTokenWithoutExpiration(id, memberRole));
    }

}
