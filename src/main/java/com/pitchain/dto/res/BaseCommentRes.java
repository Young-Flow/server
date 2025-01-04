package com.pitchain.dto.res;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class BaseCommentRes {
    private final Long commentId;
    private final boolean delYN;
    private final List<ReplyCommentRes> replyComments;

    protected BaseCommentRes(Long commentId, boolean delYN, List<ReplyCommentRes> replyComments) {
        this.commentId = commentId;
        this.delYN = delYN;
        this.replyComments = replyComments;
    }

}
