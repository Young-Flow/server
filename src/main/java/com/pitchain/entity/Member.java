package com.pitchain.entity;

import com.pitchain.common.constant.MemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    public static Member createIndividualMember(String email, String name) {
        Member member = new Member();
        member.role = MemberRole.INDIVIDUAL;
        member.email = email;
        member.name = name;

        return member;
    }

    public static Member createCompanyMember(String email) {
        Member member = new Member();
        member.role = MemberRole.COMPANY;
        member.email = email;

        return member;
    }

    public boolean hasProfileImg() {
        return profileImgKey != null && !profileImgKey.isEmpty();
    }

    public void updateProfileImgKey(String profileImgKey) {
        this.profileImgKey = profileImgKey;
    }

    public void updateProfile(String name) {
        updateName(name);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public Member(String email) {
        this.email = email;
    }
}
