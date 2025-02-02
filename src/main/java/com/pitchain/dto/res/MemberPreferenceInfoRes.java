package com.pitchain.dto.res;

import java.util.List;

public record MemberPreferenceInfoRes(
        Long memberId,
        List<PreferenceInfoRes> preferenceInfoResList
) {
}
