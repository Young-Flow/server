package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.req.CommentReq;
import com.pitchain.dto.res.BaseCommentRes;
import com.pitchain.dto.res.CommentRes;
import com.pitchain.dto.res.DeletedCommentRes;
import com.pitchain.dto.res.ReplyCommentRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Comment;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.CommentRepository;
import com.pitchain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BmRepository bmRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Transactional
    public void addComment(Long bmId, MemberDetails memberDetails, CommentReq.AddCommentReq req) {
        Member member = memberRepository.findById(memberDetails.id()).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Bm bm = bmRepository.findById(bmId).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.BM_NOT_FOUND));

        String content = req.getContent();
        Long parentCommentId = req.getParentCommentId();

        if (isOwnedBy(parentCommentId)) {
            addReplyComment(member, bm, content, parentCommentId);
        } else {
            addComment(member, bm, content);
        }
    }

    private void addReplyComment(Member member, Bm bm, String content, Long parentCommentId) {
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Comment comment = Comment.builder()
                .member(member)
                .bm(bm)
                .parentComment(parentComment)
                .content(content)
                .build();

        commentRepository.save(comment);
    }

    private void addComment(Member member, Bm bm, String content) {
        Comment comment = Comment.builder()
                .member(member)
                .bm(bm)
                .content(content)
                .build();

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<? extends BaseCommentRes> getComments(Long bmId, MemberDetails memberDetails) {
        Member member = memberRepository.findById(memberDetails.id()).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Bm bm = bmRepository.findById(bmId).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.BM_NOT_FOUND));

        List<Comment> comments = commentRepository.findByBm(bm);

        return comments.stream()
                .map(comment -> {
                    List<ReplyCommentRes> replyCommentResList = comment.getChildComments().stream()
                            .map(childComment -> ReplyCommentRes.createRes(
                                    childComment, s3Service.getFileURL(childComment.getMember().getProfileImgKey())
                            ))
                            .toList();

                    if (comment.isDelYN()) {
                        return DeletedCommentRes.createRes(comment, replyCommentResList);
                    }
                    return CommentRes.createRes(comment, replyCommentResList, s3Service.getFileURL(comment.getMember().getProfileImgKey()));
                })
                .toList();
    }

    @Transactional
    public void modifyComment(Long commentId, MemberDetails memberDetails, CommentReq.ModifyCommentReq req) {
        Member member = memberRepository.findById(memberDetails.id()).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Member commentWriter = comment.getMember();
        validateWriter(member, commentWriter);

        String content = req.getContent();
        comment.changeComment(content);
    }

    @Transactional
    public void removeComment(Long commentId, MemberDetails memberDetails) {
        Member member = memberRepository.findById(memberDetails.id()).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Member commentWriter = comment.getMember();
        validateWriter(member, commentWriter);

        if (isReplyComment(comment)) {
            commentRepository.deleteById(commentId);
        } else {
            comment.deleteParentComment();
        }
    }

    private boolean isReplyComment(Comment comment) {
        List<Comment> childComments = comment.getChildComments();
        return childComments.isEmpty();
    }

    private static void validateWriter(Member member, Member commentWriter) {
        if (!commentWriter.equals(member)) {
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);
        }
    }

    private boolean isOwnedBy(Long parentCommentId) {
        return parentCommentId != null;
    }

}
