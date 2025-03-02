package com.pitchain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BmScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bm_scrap_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bm_id", nullable = false)
    private Bm bm;

    public BmScrap(Member member, Bm bm) {
        this.member = member;
        this.bm = bm;
    }

}
