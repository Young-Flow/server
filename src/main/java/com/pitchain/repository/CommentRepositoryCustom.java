package com.pitchain.repository;

import com.pitchain.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pitchain.entity.QComment.comment;
import static com.pitchain.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<Comment> findByParentComment(List<Long> ids) {
        return queryFactory
                .selectFrom(comment)
                .join(comment.member, member).fetchJoin()
                .where(comment.parentComment.id.in(ids))
                .fetch();
    }

}
