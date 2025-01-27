package com.pitchain.entity;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(name = "EMAIL_UNIQUE", columnNames = {"email"})})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Country country;

    private String profileImgKey;

    @OneToMany(mappedBy = "member")
    private List<Investment> investments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MyCategory> myCategories = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MyBm> myBms = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CategoryPref> categoryPrefs = new ArrayList<>();

    public Member(String name, String email, Country country, String profileImgKey) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.profileImgKey = profileImgKey;
    }

    public void addCategoryPref(List<SubCategory> subCategories) {
        for (SubCategory subCategory : subCategories) {
            this.categoryPrefs.add(new CategoryPref(this, subCategory));
        }
    }
}
