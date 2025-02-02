package com.pitchain.dto.res;

import com.pitchain.dto.PreferenceInfoDto;

public record PreferenceInfoRes(
        Long bmId,
        int spViewTime,
        Boolean isViewed,
        Boolean isInvested
) {
    public static PreferenceInfoRes createRes(PreferenceInfoDto preferenceInfoDto) {
        return new PreferenceInfoRes(
                preferenceInfoDto.bmId(),
                preferenceInfoDto.spViewTime(),
                preferenceInfoDto.isViewed(),
                preferenceInfoDto.isInvested()
        );
    }
}
