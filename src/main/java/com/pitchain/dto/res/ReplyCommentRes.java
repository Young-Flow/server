package com.pitchain.dto.res;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pitchain.common.converter.S3UrlSerializer;
import com.pitchain.entity.Comment;
import com.pitchain.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReplyCommentRes(
        @NotNull
        Long commentId,
        @NotNull
        Long writerId,
        @NotEmpty
        String writerName,
        @NotEmpty
        @JsonSerialize(using = S3UrlSerializer.class)
        String writerProfileImgURL,
        @NotEmpty
        String content,
        boolean delYN,
        @Past @NotNull
        LocalDateTime createdAt,
        @Past @NotNull
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
