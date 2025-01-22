package com.pitchain.entity;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bm_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private MainCategory mainCategory;
    private String company;
    private String logoImg;
    @Column(length = 100)
    private String intro;
    @Column(length = 10000)
    private String description;

    private String descriptionImg;
    private String address;
    @Positive
    private Long valuationCap;
    @Positive
    private Integer goalInvestment;
    @Positive
    private Integer maxIssuedShare;
    private LocalDate deadline;
    private String longPitchUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "bm", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BmSubCategory> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "bm", fetch = FetchType.LAZY)
    private List<Investment> investments = new ArrayList<>();

    @OneToOne(mappedBy = "bm", fetch = FetchType.LAZY)
    private Sp sp;

    @OneToMany(mappedBy = "bm", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PtImg> ptImgs = new ArrayList<>();

    @OneToMany(mappedBy = "bm", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public double getPricePerShare() {
        return (double) valuationCap / maxIssuedShare;
    }

    @Builder
    public Bm(Member member, String name, MainCategory mainCategory, String company, String logoImg,
              String intro, String description, String descriptionImg, String address,
              Long valuationCap, Integer goalInvestment, Integer maxIssuedShare,
              LocalDate deadline, String longPitchUrl) {
        this.member = member;
        this.name = name;
        this.mainCategory = mainCategory;
        this.company = company;
        this.logoImg = logoImg;
        this.intro = intro;
        this.description = description;
        this.descriptionImg = descriptionImg;
        this.address = address;
        this.valuationCap = valuationCap;
        this.goalInvestment = goalInvestment;
        this.maxIssuedShare = maxIssuedShare;
        this.deadline = deadline;
        this.longPitchUrl = longPitchUrl;
    }

    public String getShortPitchURL() {
        if (sp == null)
            return Strings.EMPTY;
        return sp.getShortPitchURL();
    }

    public boolean isOwner(Long memberId) {
        return memberId.equals(member.getId());
    }

    public void updatePtImgs(List<PtImg> ptImgs) {
        if (ptImgs == null)
            ptImgs = new ArrayList<>();

        this.ptImgs.clear();
        this.ptImgs.addAll(ptImgs);
    }

    public void addSubCategories(List<SubCategory> subCategories) {
        if (subCategories == null) {
            subCategories = new ArrayList<>();
        }

        for (SubCategory subCategory : subCategories) {
            this.subCategories.add(new BmSubCategory(this, subCategory));
        }
    }

    public void updateSubCategories(List<SubCategory> subCategories) {
        if (subCategories == null) {
            subCategories = new ArrayList<>();
        }

        this.subCategories.clear();
        for (SubCategory subCategory : subCategories) {
            this.subCategories.add(new BmSubCategory(this, subCategory));
        }
    }

    public void update(Bm updateBm) {
        this.name = updateBm.getName();
        this.mainCategory = updateBm.getMainCategory();
        this.company = updateBm.getCompany();
        this.logoImg = updateBm.getLogoImg();
        this.intro = updateBm.getIntro();
        this.description = updateBm.getDescription();
        this.descriptionImg = updateBm.getDescriptionImg();
        this.address = updateBm.getAddress();
        this.valuationCap = updateBm.getValuationCap();
        this.goalInvestment = updateBm.getGoalInvestment();
        this.maxIssuedShare = updateBm.getMaxIssuedShare();
        this.deadline = updateBm.getDeadline();
        this.longPitchUrl = updateBm.getLongPitchUrl();
    }
}
