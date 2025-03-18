package com.pitchain.dto.res;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class BaseCommentRes {
    @NotNull
    private final Long commentId;
    private final boolean delYN;
    @NotEmpty
    private final List<ReplyCommentRes> replyComments;

    protected BaseCommentRes(Long commentId, boolean delYN, List<ReplyCommentRes> replyComments) {
        this.commentId = commentId;
        this.delYN = delYN;
        this.replyComments = replyComments;
    }

}
