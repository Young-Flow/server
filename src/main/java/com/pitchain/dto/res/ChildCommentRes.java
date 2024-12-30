package com.pitchain.dto.res;

import com.pitchain.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChildCommentRes(
        Long commentId,
        String writerName,
        String content,
        boolean deleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ChildCommentRes createRes(Comment comment) {
        return ChildCommentRes.builder()
                .commentId(comment.getId())
                .writerName(comment.getMember().getName())
                .content(comment.getContent())
                .deleted(comment.isDelYN())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}