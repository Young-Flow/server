package com.pitchain.common.payload.notification;

import com.pitchain.entity.NotificationLog;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TranscodingNotificationPayload implements NotificationPayload {
    private Long spId;
    private Long notificationId;

    public static TranscodingNotificationPayload of(Long spId, NotificationLog notificationLog) {
        TranscodingNotificationPayload payload = new TranscodingNotificationPayload();
        payload.spId = spId;
        payload.notificationId = notificationLog.getId();
        return payload;
    }
}
