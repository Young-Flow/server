package com.pitchain.jwt;

import com.pitchain.common.constant.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberClaim {
    private Long id;
    private MemberRole memberRole;
}
