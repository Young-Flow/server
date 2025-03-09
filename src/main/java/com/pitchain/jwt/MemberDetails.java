package com.pitchain.jwt;

import com.pitchain.common.constant.MemberRole;

public record MemberDetails(Long id, MemberRole memberRole) {
}
