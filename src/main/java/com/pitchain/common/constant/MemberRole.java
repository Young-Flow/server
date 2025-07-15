package com.pitchain.common.constant;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.EnumSet;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    MEMBER(EnumSet.of(RoleType.INDIVIDUAL, RoleType.COMPANY)),
    INDIVIDUAL(EnumSet.of(RoleType.INDIVIDUAL)),
    COMPANY(EnumSet.of(RoleType.COMPANY)),
    ;

    private final EnumSet<RoleType> roleTypes;

    public static MemberRole toEnum(String role) {
        try {
            return MemberRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new GeneralException(ErrorStatus.INVALID_MEMBER_ROLE);
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleTypes.stream()
                .map(roleType -> new SimpleGrantedAuthority(roleType.getRoleName()))
                .toList();
    }

    public String[] getRoles() {
        return roleTypes.stream().map(RoleType::getRoleName).toArray(String[]::new);
    }

    @Getter
    @RequiredArgsConstructor
    public enum RoleType {
        INDIVIDUAL("ROLE_INDIVIDUAL"),
        COMPANY("ROLE_COMPANY"),
        ;

        private final String roleName;
    }

}
