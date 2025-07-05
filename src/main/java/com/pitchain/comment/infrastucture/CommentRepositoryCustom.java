package com.pitchain.comment.infrastucture;

import com.pitchain.comment.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pitchain.comment.domain.QComment.comment;
import static com.pitchain.member.domain.QMember.member;


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
