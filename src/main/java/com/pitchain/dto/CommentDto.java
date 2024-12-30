package com.pitchain.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CommentDto {

    @Getter
    @Setter
    public static class addCommentDto {
        @Nullable
        private Long parentCommentId;
        private String content;
    }

    @Getter
    @Setter
    public static class modifyCommentDto {
        private String content;
    }
}


