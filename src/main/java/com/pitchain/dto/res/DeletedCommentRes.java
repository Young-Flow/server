package com.pitchain.dto.res;

import com.pitchain.entity.Comment;
import lombok.Builder;

import java.util.List;

@Builder
public record DeletedCommentRes(
        Long commentId,
        boolean delYN,
        List<ReplyCommentRes> replyComments
) {
    public static DeletedCommentRes createRes(Comment comment) {
        return DeletedCommentRes.builder()
                .commentId(comment.getId())
                .delYN(comment.isDelYN())
                .replyComments(comment.getChildComments().stream().map(ReplyCommentRes::createRes).toList())
                .build();
    }
}
