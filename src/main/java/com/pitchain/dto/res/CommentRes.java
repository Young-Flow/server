package com.pitchain.dto.res;

import com.pitchain.common.annotation.S3Url;
import com.pitchain.entity.Comment;
import com.pitchain.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentRes extends BaseCommentRes {
    @NotNull
    private final Long writerId;
    @NotEmpty
    private final String writerName;
    @NotEmpty
    @S3Url
    private final String writerProfileImgURL;
    @NotEmpty
    private final String content;
    @Past @NotNull
    private final LocalDateTime createdAt;
    @Past @NotNull
    private final LocalDateTime updatedAt;

    @Builder
    public CommentRes(
            Long commentId, boolean delYN, List<ReplyCommentRes> replyComments,
            Long writerId, String writerName, String writerProfileImgURL, String content,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        super(commentId, delYN, replyComments);
        this.writerId = writerId;
        this.writerName = writerName;
        this.writerProfileImgURL = writerProfileImgURL;
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
                .writerProfileImgURL(member.getProfileImgKey())  //추후에 JSON 직렬화 처리됨
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}
