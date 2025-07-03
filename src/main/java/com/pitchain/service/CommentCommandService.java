package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.dto.req.CommentReq;
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

@RequiredArgsConstructor
@Service
public class CommentCommandService {
    private final EntityFacade entityFacade;
    private final CommentRepository commentRepository;

    @Transactional
    public void addComment(Long bmId, MemberDetails memberDetails, CommentReq.AddCommentReq req) {

        Member member = entityFacade.getMember(memberDetails.id());
        Bm bm = entityFacade.getBm(bmId);

        String content = req.getContent();
        Long parentCommentId = req.getParentCommentId();

        if (hasParentComment(parentCommentId)) {
            addReplyComment(bm, member, content, parentCommentId);
        } else {
            addComment(member, bm, content);
        }
    }

    private void addComment(Member member, Bm bm, String content) {
        Comment comment = Comment.of(member, bm, content);

        commentRepository.save(comment);
    }

    private void addReplyComment(Bm bm, Member member, String content, Long parentCommentId) {
        Comment parentComment = entityFacade.getComment(parentCommentId);

        Comment comment = Comment.of(member, bm, content);
        comment.setParent(parentComment);

        commentRepository.save(comment);
    }

    private boolean hasParentComment(Long parentCommentId) {
        return parentCommentId != null;
    }

    @Transactional
    public void modifyComment(Long commentId, MemberDetails memberDetails, CommentReq.ModifyCommentReq req) {
        Member member = entityFacade.getMember(memberDetails.id());
        Comment comment = entityFacade.getComment(commentId);

        Member commentWriter = comment.getMember();
        validateWriter(member, commentWriter);

        String content = req.getContent();
        comment.changeComment(content);
    }

    private static void validateWriter(Member member, Member commentWriter) {
        if (!commentWriter.equals(member)) {
            throw new GeneralException(ErrorStatus.MEMBER_FORBIDDEN);
        }
    }

    @Transactional
    public void removeComment(Long commentId, MemberDetails memberDetails) {
        Member member = entityFacade.getMember(memberDetails.id());
        Comment comment = entityFacade.getComment(commentId);

        Member commentWriter = comment.getMember();
        validateWriter(member, commentWriter);

        if (isReplyComment(comment)) {
            commentRepository.deleteById(commentId);
        } else {
            comment.deleteParentComment();
        }
    }

    private boolean isReplyComment(Comment comment) {
        List<Comment> replyComments = commentRepository.findByParentComment(comment);
        return replyComments.isEmpty();
    }
}
