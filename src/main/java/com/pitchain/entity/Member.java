package com.pitchain.entity;

import com.pitchain.common.constant.MemberRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String email;

    private String profileImgKey;

    private String name;

    public static Member from(String email, String name) {
        return Member.builder()
                .role(MemberRole.INDIVIDUAL)
                .email(email)
                .name(name)
                .build();
    }
}
