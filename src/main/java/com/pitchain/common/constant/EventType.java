package com.pitchain.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventType {
    TRANSCODING_COMPLETED("업로드에 성공했습니다"),
    TRANSCODING_FAILED("업로드에 실패했습니다");

    private String description;
}


