package com.pitchain.common.apiPayload;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"code", "message"})
public class CustomResponse {

    private final String code;
    private final String message;

    public static CustomResponse onFailure(String code, String message) {
        return new CustomResponse(code, message);
    }
}
