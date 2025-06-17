package com.pitchain.common.constant;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import lombok.Getter;

@Getter
public enum SpStatus {
    TRANSCODING, TRANSCODED, FAILED;

    public static SpStatus fromEventType(EventType eventType) {
        if (eventType == EventType.TRANSCODING_COMPLETED) {
            return TRANSCODED;
        } else if (eventType == EventType.TRANSCODING_FAILED) {
            return FAILED;
        }
        throw new GeneralException(ErrorStatus.SP_STATUS_CONVERSION_FAILED);
    }
}
