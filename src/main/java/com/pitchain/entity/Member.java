package com.pitchain.entity;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.constant.SubCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<BmScrap> bmScraps = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<SpLike> spLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CategoryPref> categoryPrefs = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Investment> investments = new ArrayList<>();

    public static Member createIndividualMember(String email, String name) {
        return Member.builder()
                .role(MemberRole.INDIVIDUAL)
                .email(email)
                .name(name)
                .build();
    }

    public static Member createCompanyMember(String email) {
        return Member.builder()
                .role(MemberRole.COMPANY)
                .email(email)
                .build();
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

    public void addCategoryPref(List<SubCategory> subCategories) {
        for (SubCategory subCategory : subCategories) {
            this.categoryPrefs.add(new CategoryPref(this, subCategory));
        }
    }

    public Member(String email) {
        this.email = email;
    }
}
