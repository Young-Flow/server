package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.dto.req.CommentReq;
import com.pitchain.dto.res.BaseCommentRes;
import com.pitchain.dto.res.CommentRes;
import com.pitchain.dto.res.DeletedCommentRes;
import com.pitchain.dto.res.ReplyCommentRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Comment;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.CommentRepository;
import com.pitchain.repository.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EntityFacade entityFacade;

    @Transactional
    public void addComment(Long bmId, MemberDetails memberDetails, CommentReq.AddCommentReq req) {
        Member member = entityFacade.getMember(memberDetails.id());
        Bm bm = entityFacade.getBm(bmId);

        String content = req.getContent();
        Long parentCommentId = req.getParentCommentId();

        if (isOwnedBy(parentCommentId)) {
            addReplyComment(member, bm, content, parentCommentId);
        } else {
            addComment(member, bm, content);
        }
    }

    @Transactional
    private void addReplyComment(Member member, Bm bm, String content, Long parentCommentId) {
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() ->
                new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        Comment comment = Comment.of(member, bm, content);
        comment.setParent(parentComment);

        commentRepository.save(comment);
    }

    @Transactional
    private void addComment(Member member, Bm bm, String content) {
        Comment comment = Comment.of(member, bm, content);

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<? extends BaseCommentRes> getComments(Long bmId, MemberDetails memberDetails) {
        Member member = entityFacade.getMember(memberDetails.id());
        Bm bm = entityFacade.getBm(bmId);

        List<Comment> comments = commentRepository.findAllByBm(bm);

        return comments.stream()
                .map(comment -> {
                    List<ReplyCommentRes> replyCommentResList = commentRepository.findByParentComment(comment).stream()
                            .map(childComment -> ReplyCommentRes.createRes(
                                    childComment, childComment.getMember().getProfileImgKey()
                            ))
                            .toList();

                    if (comment.isDelYN()) {
                        return DeletedCommentRes.createRes(comment, replyCommentResList);
                    }
                    return CommentRes.createRes(comment, replyCommentResList);
                })
                .toList();
    }

    @Transactional
    public void modifyComment(Long commentId, MemberDetails memberDetails, CommentReq.ModifyCommentReq req) {
        Member member = entityFacade.getMember(memberDetails.id());
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        Member commentWriter = comment.getMember();
        validateWriter(member, commentWriter);

        String content = req.getContent();
        comment.changeComment(content);
    }

    @Transactional
    public void removeComment(Long commentId, MemberDetails memberDetails) {
        Member member = entityFacade.getMember(memberDetails.id());
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        Member commentWriter = comment.getMember();
        validateWriter(member, commentWriter);

        if (isReplyComment(comment)) {
            commentRepository.deleteById(commentId);
        } else {
            comment.deleteParentComment();
        }
    }

    private boolean isReplyComment(Comment comment) {
        List<Comment> childComments = commentRepository.findByParentComment(comment);
        return childComments.isEmpty();
    }

    private static void validateWriter(Member member, Member commentWriter) {
        if (!commentWriter.equals(member)) {
            throw new GeneralException(ErrorStatus.MEMBER_FORBIDDEN);
        }
    }

    private boolean isOwnedBy(Long parentCommentId) {
        return parentCommentId != null;
    }

}
