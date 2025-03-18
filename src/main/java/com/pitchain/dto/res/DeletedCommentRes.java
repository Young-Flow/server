package com.pitchain.dto.res;

import com.pitchain.entity.Comment;
import lombok.Builder;

import java.util.List;

public class DeletedCommentRes extends BaseCommentRes {

    @Builder
    public DeletedCommentRes(Long commentId, boolean delYN, List<ReplyCommentRes> replyComments) {
        super(commentId, delYN, replyComments);
    }

    public static DeletedCommentRes createRes(Comment comment, List<ReplyCommentRes> replyComments) {
        return DeletedCommentRes.builder()
                .commentId(comment.getId())
                .delYN(comment.isDelYN())
                .replyComments(replyComments)
                .build();
    }
}
