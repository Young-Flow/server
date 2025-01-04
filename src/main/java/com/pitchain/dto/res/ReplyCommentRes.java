package com.pitchain.dto.res;

import com.pitchain.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReplyCommentRes(
        Long commentId,
        Long writerId,
        String writerName,
        String writerProfileImg,
        String content,
        boolean delYN,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReplyCommentRes createRes(Comment comment) {
        return ReplyCommentRes.builder()
                .commentId(comment.getId())
                .writerId(comment.getMember().getId())
                .writerName(comment.getMember().getName())
                .writerProfileImg(comment.getMember().getProfileImg())
                .content(comment.getContent())
                .delYN(comment.isDelYN())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}