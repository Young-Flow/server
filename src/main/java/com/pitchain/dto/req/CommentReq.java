package com.pitchain.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CommentReq {

    @Getter
    @Setter
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    public static class AddCommentReq {
        @Nullable
        private Long parentCommentId;
        private String content;
    }

    @Getter
    @Setter
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    public static class ModifyCommentReq {
        private String content;
    }
}


