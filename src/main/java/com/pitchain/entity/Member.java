package com.pitchain.entity;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.OauthProvider;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.entity.BaseEntity;
import com.pitchain.oauth2.member.OauthMemberInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Country country;

    private String profileImgKey;

    private String socialId;

    private OauthProvider oauthProvider;

    @OneToMany(mappedBy = "member")
    private List<Investment> investments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MyBm> myBms = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryPref> categoryPrefs = new ArrayList<>();

    public Member(String name, Country country, String profileImgKey) {
        this.name = name;
        this.country = country;
        this.profileImgKey = profileImgKey;
    }

    public Member(OauthMemberInfo request) {
        this.socialId = request.getSocialId();
        this.oauthProvider = request.getOauthProvider();
    }

    public void addCategoryPref(List<SubCategory> subCategories) {
        for (SubCategory subCategory : subCategories) {
            this.categoryPrefs.add(new CategoryPref(this, subCategory));
        }
    }
}
