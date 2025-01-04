package com.pitchain.dto.res;

import com.pitchain.entity.Comment;
import com.pitchain.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentRes extends BaseCommentRes {
    private final Long writerId;
    private final String writerName;
    private final String writerProfileImg;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public CommentRes(
            Long commentId, boolean delYN, List<ReplyCommentRes> replyComments,
            Long writerId, String writerName, String writerProfileImg, String content,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        super(commentId, delYN, replyComments);
        this.writerId = writerId;
        this.writerName = writerName;
        this.writerProfileImg = writerProfileImg;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentRes createRes(Comment comment, List<ReplyCommentRes> replyComments) {
        Member member = comment.getMember();
        return CommentRes.builder()
                .commentId(comment.getId())
                .delYN(comment.isDelYN())
                .replyComments(replyComments)
                .writerId(member.getId())
                .writerName(member.getName())
                .writerProfileImg(member.getProfileImg())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}
