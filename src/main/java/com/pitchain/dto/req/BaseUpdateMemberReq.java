package com.pitchain.dto.req;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pitchain.common.constant.Country;
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
    @NotNull
    private final MemberRole memberRole;
    private final String email;
    private final String name;
    private final Country country;

    public BaseUpdateMemberReq(MemberRole memberRole, String email, String name, Country country) {
        this.memberRole = memberRole;
        this.email = email;
        this.name = name;
        this.country = country;
    }

}
