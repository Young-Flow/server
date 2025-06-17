package com.pitchain.common.constant;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;

public enum NotificationType {
    SUBSCRIBE_SUCCESS, TRANSCODING_COMPLETED, TRANSCODING_FAILED,
    ;

    public static NotificationType fromEventType(EventType eventType) {
        if (eventType == EventType.TRANSCODING_COMPLETED) {
            return TRANSCODING_COMPLETED;
        } else if (eventType == EventType.TRANSCODING_FAILED) {
            return TRANSCODING_FAILED;
        }
        throw new GeneralException(ErrorStatus.NOTIFICATION_TYPE_CONVERSION_FAILED);
    }
}
