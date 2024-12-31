package com.pitchain.dto.res;

import com.pitchain.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ParentCommentRes(
        Long commentId,
        String writerName,
        String content,
        boolean deleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ChildCommentRes> childComment
) {
    public static ParentCommentRes createRes(Comment comment) {
        return ParentCommentRes.builder()
                .commentId(comment.getId())
                .writerName(comment.getMember().getName())
                .childComment(comment.getChildComments().stream().map(ChildCommentRes::createRes).toList())
                .content(comment.getContent())
                .deleted(comment.isDelYN())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}
