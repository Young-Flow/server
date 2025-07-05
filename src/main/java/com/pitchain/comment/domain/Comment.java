package com.pitchain.comment.domain;

import com.pitchain.bm.domain.Bm;
import com.pitchain.common.entity.BaseEntity;
import com.pitchain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bm_id", nullable = false)
    private Bm bm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Column(nullable = false)
    private String content;

    @Column(name = "del_yn")
    private boolean delYN;

    public static Comment of(Member member, Bm bm, String content) {
        Comment comment = new Comment();
        comment.member = member;
        comment.bm = bm;
        comment.content = content;
        return comment;
    }

    public void setParent(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void deleteParentComment() {
        this.delYN = true;
    }

    public void changeComment(String content) {
        this.content = content;
    }
}
