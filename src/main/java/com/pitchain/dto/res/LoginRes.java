package com.pitchain.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRes {
    @Schema(required = true)
    private String accessToken;
    @Schema(required = true)
    private String refreshToken;

    public static LoginRes createRes(String accessToken, String refreshToken) {
        return new LoginRes(accessToken, refreshToken);
    }
}
