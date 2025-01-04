package com.pitchain.dto.res;

import com.pitchain.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentRes(
        Long commentId,
        Long writerId,
        String writerName,
        String writerProfileImg,
        String content,
        boolean delYN,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ReplyCommentRes> replyComments
) {
    public static CommentRes createRes(Comment comment) {
        return CommentRes.builder()
                .commentId(comment.getId())
                .writerId(comment.getMember().getId())
                .writerName(comment.getMember().getName())
                .writerProfileImg(comment.getMember().getProfileImg())
                .replyComments(comment.getChildComments().stream().map(ReplyCommentRes::createRes).toList())
                .content(comment.getContent())
                .delYN(comment.isDelYN())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}
