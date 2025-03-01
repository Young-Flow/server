package com.pitchain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    private String name;

    private String address;

    private String logoImgKey;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Company(String name, String address, String logoImgKey, Member member) {
        this.name = name;
        this.address = address;
        this.logoImgKey = logoImgKey;
        this.member = member;
    }

    public void updateCompanyInfo(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public boolean isOwner(Member member) {
        return this.member.equals(member);
    }

    public boolean hasLogoImg() {
        return logoImgKey != null || !logoImgKey.isEmpty();
    }

    public void updateLogoImgKey(String logoImgKey) {
        this.logoImgKey = logoImgKey;
    }
}
