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

    private String email;

    private String password;

    private Boolean isVerified; //todo 추후 사업지 등록증을 통한 인증으로 변경 예정

    private String name;

    private String address;

    private String logoImgKey;

    public static Company createUnverifiedCompany(String email, String encodedPassword) {
        return new Company(email, encodedPassword);
    }

    public Company(String email, String encodedPassword) {
        this.email = email;
        this.password = encodedPassword;
        this.isVerified = false;
    }

    public Company(String email, String password, Boolean isVerified, String name, String address, String logoImgKey) {
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
        this.name = name;
        this.address = address;
        this.logoImgKey = logoImgKey;
    }

    public void updateCompanyInfo(String name, String address) {
        this.name = name;
        this.address = address;
        this.isVerified = true;
    }

    public boolean hasLogoImg() {
        return logoImgKey != null && !logoImgKey.isEmpty();
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateLogoImgKey(String logoImgKey) {
        this.logoImgKey = logoImgKey;
    }
}
