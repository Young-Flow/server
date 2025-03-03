package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.req.OauthLoginReq;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.service.OauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "소셜 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2")
public class OauthController {
    private final OauthService oauthService;

    @GetMapping("/")

    @Operation(summary = "소셜 로그인", description = "KAKAO: https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=891063e7c569324189a0353dfb18c534&redirect_uri={redirectURL}을 통해 code값 받아오기, " +
                                                    "NAVER: https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=T4z00JRW0P38KpqrIc6w&state={state}&redirect_uri={redirectURL}을 통해 code값 받아오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "로그인 성공"),
            @ApiResponse(responseCode = "COMMON400", description = "처리할 수 없는 소셜 로그인")}
    )
    @PostMapping("/login")
    public CustomApiResponse<LoginRes> socialLogin(@Valid @RequestBody OauthLoginReq req) {
        LoginRes memberByOauthLogin = oauthService.getMemberByOauthLogin(req);
        return CustomApiResponse.onSuccess(memberByOauthLogin);
    }
}
