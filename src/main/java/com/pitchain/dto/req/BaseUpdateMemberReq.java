package com.pitchain.dto.req;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pitchain.common.constant.MemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "memberRole", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateIndividualReq.class, name = "INDIVIDUAL"),
        @JsonSubTypes.Type(value = UpdateCompanyReq.class, name = "COMPANY")
})
@Getter
public abstract class BaseUpdateMemberReq {
    @NotNull(message = "회원 역할은 필수입니다.")
    private final MemberRole memberRole;

    public BaseUpdateMemberReq(MemberRole memberRole) {
        this.memberRole = memberRole;
    }

}
