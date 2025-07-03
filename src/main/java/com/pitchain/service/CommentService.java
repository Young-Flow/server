package com.pitchain.service;

import com.pitchain.dto.req.CommentReq;
import com.pitchain.dto.res.BaseCommentRes;
import com.pitchain.jwt.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @Transactional
    public void addComment(Long bmId, MemberDetails memberDetails, CommentReq.AddCommentReq req) {
        commentCommandService.addComment(bmId, memberDetails, req);
    }

    @Transactional(readOnly = true)
    public List<? extends BaseCommentRes> getComments(Long bmId, MemberDetails memberDetails) {
        return commentQueryService.getComments(bmId, memberDetails);
    }

    @Transactional
    public void modifyComment(Long commentId, MemberDetails memberDetails, CommentReq.ModifyCommentReq req) {
        commentCommandService.modifyComment(commentId, memberDetails, req);
    }

    @Transactional
    public void removeComment(Long commentId, MemberDetails memberDetails) {
        commentCommandService.removeComment(commentId, memberDetails);
    }

}
