package com.pitchain.entity;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public double getPricePerShare() {
        return (double) valuationCap / maxIssuedShare;
    }

    public static Bm create(Company company, String name, MainCategory mainCategory,
                              String intro, String description, String descImgKey, String address,
                              Long valuationCap, Long goalInvestment, Integer maxIssuedShare,
                              LocalDate deadline, String longPitchURL) {
        Bm bm = new Bm();
        bm.company = company;
        bm.name = name;
        bm.mainCategory = mainCategory;
        bm.intro = intro;
        bm.description = description;
        bm.descImgKey = descImgKey;
        bm.address = address;
        bm.valuationCap = valuationCap;
        bm.goalInvestment = goalInvestment;
        bm.maxIssuedShare = maxIssuedShare;
        bm.deadline = deadline;
        bm.longPitchURL = longPitchURL;
        return bm;
    }

    public boolean isOwner(Long companyId) {
        return this.company.getId().equals(companyId);
    }

    public void update(String name, MainCategory mainCategory, String intro,
                       String description, String descImgKey, String address,
                       Long valuationCap, Long goalInvestment, Integer maxIssuedShare,
                       LocalDate deadline, String longPitchURL) {
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

}
