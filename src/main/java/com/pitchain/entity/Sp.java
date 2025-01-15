package com.pitchain.entity;

import com.pitchain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sp_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bm_id", nullable = false)
    private Bm bm;

    @Column(nullable = false)
    private String shortPitchURL;

    @Column(nullable = false)
    private String thumbnailImg;

    private int views;

    @Column(nullable = false)
    private String name;

    public Sp(Bm bm, String shortPitchURL, String thumbnailImg, String name) {
        this.bm = bm;
        this.shortPitchURL = shortPitchURL;
        this.thumbnailImg = thumbnailImg;
        this.name = name;
        this.views = 0;
    }

    public boolean isOwner(Long memberId) {
        return bm.getMember().getId().equals(memberId);
    }

    public void update(Sp updateSp) {
        this.shortPitchURL = updateSp.shortPitchURL;
        this.thumbnailImg = updateSp.thumbnailImg;
        this.name = updateSp.name;
    }
}
