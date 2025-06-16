package com.pitchain.controller;

import com.pitchain.dto.req.OauthLoginReq;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.service.OauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "소셜 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2")
public class OauthController {
    private final OauthService oauthService;

    @Operation(summary = "소셜 로그인", description = "각 Oauth 플랫폼의 endpoint를 통해 code값  조회 후 api 호출 <br>" +
            "KAKAO: https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=891063e7c569324189a0353dfb18c534&redirect_uri=http://localhost:5173/sign/kakao/callback <br> " +
            "NAVER: https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=T4z00JRW0P38KpqrIc6w&redirect_uri=http://localhost:5173/sign/naver/callback <br> " +
            "GOOGLE: https://accounts.google.com/o/oauth2/v2/auth?client_id=811738095331-ogbk4dfq2bg5ojo6gf9u3oa4iob80glb.apps.googleusercontent.com&redirect_uri=http://localhost:5173/sign/google/callback&response_type=code&scope=email%20profile"
    )
    @PostMapping("/login")
    public LoginRes socialLogin(@Valid @RequestBody OauthLoginReq req) {
        LoginRes loginRes = oauthService.oauthLogin(req);
        return loginRes;
    }
}
