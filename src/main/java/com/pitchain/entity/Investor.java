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
public class Investor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investor_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Country country;

    private String socialId;

    private OauthProvider oauthProvider;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "member")
    private List<Investment> investments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BmScrap> bmScraps = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<SpLike> spLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryPref> categoryPrefs = new ArrayList<>();

    public Investor(Country country, String profileImgKey) {
        this.country = country;
        this.profileImgKey = profileImgKey;
    }

    public Investor(OauthMemberInfo request) {
        this.socialId = request.getSocialId();
        this.email = request.getEmail();
        this.nickname = request.getNickname();
        this.oauthProvider = request.getOauthProvider();
    }

    public void addCategoryPref(List<SubCategory> subCategories) {
        for (SubCategory subCategory : subCategories) {
            this.categoryPrefs.add(new CategoryPref(this, subCategory));
        }
    }
}
