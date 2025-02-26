package com.pitchain.dto.res;

import com.pitchain.entity.Comment;
import com.pitchain.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReplyCommentRes(
        Long commentId,
        Long writerId,
        String writerNickname,
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
                .writerNickname(member.getNickname())
                .content(comment.getContent())
                .delYN(comment.isDelYN())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
