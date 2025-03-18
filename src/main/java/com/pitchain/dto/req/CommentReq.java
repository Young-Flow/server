package com.pitchain.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
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
        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String content;
    }

    @Getter
    @Setter
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    public static class ModifyCommentReq {
        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String content;
    }
}


