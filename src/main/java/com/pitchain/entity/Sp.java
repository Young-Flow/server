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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bm_id", nullable = false)
    private Bm bm;

    @Column(nullable = false)
    private String spKey;

    @Column(nullable = false)
    private String thumbnailImgKey;

    private int views;

    @Column(nullable = false)
    private String name;

    public Sp(Bm bm, String spKey, String thumbnailImgKey, String name) {
        this.bm = bm;
        this.spKey = spKey;
        this.thumbnailImgKey = thumbnailImgKey;
        this.name = name;
        this.views = 0;
    }

    public boolean isOwner(Long companyId) {
        return bm.getCompany().getId().equals(companyId);
    }

    public void update(Sp updateSp) {
        this.spKey = updateSp.spKey;
        this.thumbnailImgKey = updateSp.thumbnailImgKey;
        this.name = updateSp.name;
    }
}
