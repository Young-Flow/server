package com.pitchain.common.security;

import com.pitchain.common.constant.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberClaims {
    private Long id;
    private MemberRole memberRole;
}
