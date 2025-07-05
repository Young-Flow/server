package com.pitchain.oauth.application.res;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRes {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;

    public static LoginRes createRes(String accessToken, String refreshToken) {
        return new LoginRes(accessToken, refreshToken);
    }
}
