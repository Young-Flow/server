package com.pitchain.common.payload.event;

import lombok.Getter;

@Getter
public class TranscodingEventPayload implements EventPayload {
    private Long spId;
}
