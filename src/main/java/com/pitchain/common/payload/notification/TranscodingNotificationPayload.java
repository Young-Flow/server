package com.pitchain.common.payload.notification;

import com.pitchain.notification.domain.NotificationHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TranscodingNotificationPayload implements NotificationPayload {
    private Long spId;
    private Long notificationId;

    public static TranscodingNotificationPayload of(Long spId, NotificationHistory notificationHistory) {
        TranscodingNotificationPayload payload = new TranscodingNotificationPayload();
        payload.spId = spId;
        payload.notificationId = notificationHistory.getId();
        return payload;
    }
}
