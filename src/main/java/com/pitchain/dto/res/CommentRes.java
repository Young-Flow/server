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
    private final String writerNickname;
    private final String writerProfileImgURL;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public CommentRes(
            Long commentId, boolean delYN, List<ReplyCommentRes> replyComments,
            Long writerId, String writerNickname, String writerProfileImgURL, String content,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        super(commentId, delYN, replyComments);
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.writerProfileImgURL = writerProfileImgURL;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentRes createRes(Comment comment, List<ReplyCommentRes> replyComments, String writerProfileImgURL) {
        Member member = comment.getMember();
        return CommentRes.builder()
                .commentId(comment.getId())
                .delYN(comment.isDelYN())
                .replyComments(replyComments)
                .writerId(member.getId())
                .writerNickname(member.getNickname())
                .writerProfileImgURL(writerProfileImgURL)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}
