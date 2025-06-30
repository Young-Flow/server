package com.pitchain.common.constant;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    MEMBER(new String[]{"ROLE_INDIVIDUAL", "ROLE_COMPANY"}),
    INDIVIDUAL(new String[]{"ROLE_INDIVIDUAL"}),
    COMPANY(new String[]{"ROLE_COMPANY"}),
    ;

    private final String[] roles;

    public static MemberRole toEnum(String role) {
        try {
            return MemberRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new GeneralException(ErrorStatus.INVALID_MEMBER_ROLE);
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(roles).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
