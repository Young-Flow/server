package com.pitchain.common.payload.discord;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LongTimeRequestDiscordPayload implements DiscordPayload {
    private String url;
    private Long turnaroundTimeSec;
    private String requestId;

    public static LongTimeRequestDiscordPayload of(String requestId, String url, Long time) {
        LongTimeRequestDiscordPayload payload = new LongTimeRequestDiscordPayload();
        payload.requestId = requestId;
        payload.url = url;
        payload.turnaroundTimeSec = time;
        return payload;
    }
}
