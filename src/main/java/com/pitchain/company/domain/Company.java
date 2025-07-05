package com.pitchain.company.domain;

import com.pitchain.member.domain.Member;
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

    private String password;

    private Boolean isVerified; //todo 추후 사업지 등록증을 통한 인증으로 변경 예정

    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static Company createUnverifiedCompany(Member member, String encodedPassword) {
        return new Company(member, encodedPassword);
    }

    public Company(Member member, String encodedPassword) {
        this.member = member;
        this.password = encodedPassword;
        this.isVerified = false;
    }

    public Company(String password, Boolean isVerified, String address) {
        this.password = password;
        this.isVerified = isVerified;
        this.address = address;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updatePassword(String encodedNewPassword) {
        this.password = encodedNewPassword;
    }

    public void verifyCompany() {
        this.isVerified = true;
    }

    public Boolean IsVerified() {
        return isVerified;
    }

    public boolean isOwner(Long companyId) {
        return this.id.equals(companyId);
    }
}
