package com.pitchain.dto;

public record PreferenceInfoDto(
        Long memberId,
        Long bmId,
        int spViewTime,
        Boolean isViewed,
        Boolean isInvested
) {
}
