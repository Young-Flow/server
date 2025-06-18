package com.pitchain.common.payload.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class SuccessSubscribeNotificationPayload implements NotificationPayload {
    private static final String MESSAGE_FORMAT = "EventStream Created. [memberId = %d]";

    private String message;

    public static SuccessSubscribeNotificationPayload of(Long memberId) {
        SuccessSubscribeNotificationPayload payload = new SuccessSubscribeNotificationPayload();
        payload.message = MESSAGE_FORMAT.formatted(memberId);
        return payload;
    }
}
