package com.pitchain.common.payload.discord;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ErrorDiscordPayload implements DiscordPayload {
    private String url;
    private String parameterMessage;
    private String stackTrace;
    private String requestId;

    public static ErrorDiscordPayload of(String requestId, String fullPath, String parameterMessage, String sampleStackTrace) {
        ErrorDiscordPayload payload = new ErrorDiscordPayload();
        payload.url = fullPath;
        payload.parameterMessage = parameterMessage;
        payload.stackTrace = sampleStackTrace;
        payload.requestId = requestId;
        return payload;
    }
}
