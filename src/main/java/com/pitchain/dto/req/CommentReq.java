package com.pitchain.dto.req;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CommentReq {

    @Getter
    @Setter
    public static class AddCommentReq {
        @Nullable
        private Long parentCommentId;
        private String content;
    }

    @Getter
    @Setter
    public static class ModifyCommentReq {
        private String content;
    }
}


