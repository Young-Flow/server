package com.pitchain.mysphistory.domain;

import com.pitchain.common.entity.BaseEntity;
import com.pitchain.bm.domain.Bm;
import com.pitchain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MySpHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_sp_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bm_id")
    private Bm bm;

    @Column(nullable = false)
    private int viewTime;  //단위: ms

    public MySpHistory(Member member, Bm bm, int viewTime) {
        this.member = member;
        this.bm = bm;
        this.viewTime = viewTime;
    }

    public void updateViewTime(int viewTime) {
        this.viewTime = viewTime;
    }
}
