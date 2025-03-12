package com.pitchain.dto.res;

import com.pitchain.entity.Comment;
import com.pitchain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public record ReplyCommentRes(
        Long commentId,
        Long writerId,
        String writerName,
        String writerProfileImgURL,
        String content,
        boolean delYN,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReplyCommentRes createRes(Comment comment, String writerProfileImgURL) {
        Member member = comment.getMember();
        return ReplyCommentRes.builder()
                .commentId(comment.getId())
                .writerId(member.getId())
                .writerProfileImgURL(writerProfileImgURL)
                .writerName(member.getName())
                .content(comment.getContent())
                .delYN(comment.isDelYN())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
