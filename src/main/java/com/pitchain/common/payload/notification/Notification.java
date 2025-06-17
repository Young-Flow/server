package com.pitchain.common.payload.notification;

import com.pitchain.common.constant.NotificationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification<T extends NotificationPayload> {
    private NotificationType notificationType;
    private T payload;

    public static <T extends NotificationPayload> Notification<T> of(NotificationType notificationType, T payload) {
        Notification<T> notification = new Notification<>();
        notification.notificationType = notificationType;
        notification.payload = payload;
        return notification;
    }

    public static Notification<SuccessSubscribeNotificationPayload> ofSuccessSubscribe(Long memberId) {
        Notification<SuccessSubscribeNotificationPayload> notification = new Notification<>();
        notification.notificationType = NotificationType.SUBSCRIBE_SUCCESS;
        notification.payload = SuccessSubscribeNotificationPayload.of(memberId);
        return notification;
    }
}