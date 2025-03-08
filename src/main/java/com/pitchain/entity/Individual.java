package com.pitchain.entity;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.OauthProvider;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.entity.BaseEntity;
import com.pitchain.oauth2.member.OauthMemberInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table
public class Individual extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investor_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Country country = Country.ROK;

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

    public static Individual createInvestor(OauthMemberInfo oauthMemberInfo) {
        return Individual.builder()
                .socialId(oauthMemberInfo.getSocialId())
                .oauthProvider(oauthMemberInfo.getOauthProvider())
                .build();
    }

    public void addCategoryPref(List<SubCategory> subCategories) {
        for (SubCategory subCategory : subCategories) {
            this.categoryPrefs.add(new CategoryPref(this, subCategory));
        }
    }
}
