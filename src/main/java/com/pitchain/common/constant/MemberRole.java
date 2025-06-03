package com.pitchain.common.constant;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;

public enum MemberRole {
    COMPANY, INDIVIDUAL;

    public static MemberRole toEnum(String role) {
        try {
            return MemberRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new GeneralException(ErrorStatus.INVALID_MEMBER_ROLE);
        }
    }
}
