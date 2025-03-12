package com.pitchain.common.constant;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;

public enum MemberRole {
    COMPANY, INDIVIDUAL;

    public static MemberRole toEnum(String role) {
        try {
            return MemberRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new GeneralHandler(ErrorStatus.INVALID_MEMBER_ROLE);
        }
    }
}
