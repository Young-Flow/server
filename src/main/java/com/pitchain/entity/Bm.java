package com.pitchain.entity;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Bm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bm_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private MainCategory mainCategory;
    @Column(length = 100)
    private String intro;
    @Column(length = 10000)
    private String description;
    private String descImgKey;
    private String address;
    @Positive
    private Long valuationCap;
    @Positive
    private Long goalInvestment;
    @Positive
    private Integer maxIssuedShare;
    private LocalDate deadline;
    @Column(name = "long_pitch_url")
    private String longPitchURL;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "bm", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BmSubCategory> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "bm", fetch = FetchType.LAZY)
    private List<Investment> investments = new ArrayList<>();

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "bm", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sp> sps = new ArrayList<>();

    @OneToMany(mappedBy = "bm", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PtImg> ptImgs = new ArrayList<>();

    @OneToMany(mappedBy = "bm", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public double getPricePerShare() {
        return (double) valuationCap / maxIssuedShare;
    }

    @Builder
    public Bm(Member member, Company company, String name, MainCategory mainCategory,
              String intro, String description, String descImgKey, String address,
              Long valuationCap, Long goalInvestment, Integer maxIssuedShare,
              LocalDate deadline, String longPitchURL) {
        this.member = member;
        this.company = company;
        this.name = name;
        this.mainCategory = mainCategory;
        this.intro = intro;
        this.description = description;
        this.descImgKey = descImgKey;
        this.address = address;
        this.valuationCap = valuationCap;
        this.goalInvestment = goalInvestment;
        this.maxIssuedShare = maxIssuedShare;
        this.deadline = deadline;
        this.longPitchURL = longPitchURL;
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
        for (SubCategory subCategory : subCategories) {
            this.subCategories.add(new BmSubCategory(this, subCategory));
        }
    }

    public void updateSubCategories(List<SubCategory> subCategories) {
        this.subCategories.clear();
        addSubCategories(subCategories);
    }

    public void update(Bm updateBm) {
        this.name = updateBm.getName();
        this.mainCategory = updateBm.getMainCategory();
        this.intro = updateBm.getIntro();
        this.description = updateBm.getDescription();
        this.descImgKey = updateBm.getDescImgKey();
        this.address = updateBm.getAddress();
        this.valuationCap = updateBm.getValuationCap();
        this.goalInvestment = updateBm.getGoalInvestment();
        this.maxIssuedShare = updateBm.getMaxIssuedShare();
        this.deadline = updateBm.getDeadline();
        this.longPitchURL = updateBm.getLongPitchURL();
    }

    public List<String> getKoreanSubCategories() {
        return this.subCategories.stream()
                .map(bmSubCategory -> bmSubCategory.getSubCategory().getKoreanName())
                .toList();
    }
}
