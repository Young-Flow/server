package com.pitchain.dto.res;

import com.pitchain.entity.Comment;
import com.pitchain.entity.Member;
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
        Member member = comment.getMember();
        return ReplyCommentRes.builder()
                .commentId(comment.getId())
                .writerId(member.getId())
                .writerProfileImg(member.getProfileImg())
                .writerName(member.getName())
                .content(comment.getContent())
                .delYN(comment.isDelYN())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
