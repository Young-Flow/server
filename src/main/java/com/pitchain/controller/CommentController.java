package com.pitchain.controller;

import com.pitchain.dto.req.CommentReq;
import com.pitchain.dto.res.BaseCommentRes;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

;

@RestController
@RequestMapping("/bms")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{bmId}/comments")
    public void addComment(@PathVariable("bmId") Long bmId,
                           @AuthenticationPrincipal MemberDetails memberDetails,
                           @RequestBody CommentReq.AddCommentReq req) {
        commentService.addComment(bmId, memberDetails, req);
    }

    @GetMapping("/{bmId}/comments")
    public List<? extends BaseCommentRes> getComments(@PathVariable("bmId") Long bmId,
                                                      @AuthenticationPrincipal MemberDetails memberDetails) {
        List<? extends BaseCommentRes> comments = commentService.getComments(bmId, memberDetails);
        return comments;
    }

    @PutMapping("/{bmId}/comments/{commentId}")
    public void modifyComment(@PathVariable("bmId") Long bmId,
                              @PathVariable("commentId") Long commentId,
                              @AuthenticationPrincipal MemberDetails memberDetails,
                              @RequestBody CommentReq.ModifyCommentReq req) {
        commentService.modifyComment(commentId, memberDetails, req);
    }

    @DeleteMapping("/{bmId}/comments/{commentId}")
    public void removeComment(@PathVariable("bmId") Long bmId,
                              @PathVariable("commentId") Long commentId,
                              @AuthenticationPrincipal MemberDetails memberDetails) {
        commentService.removeComment(commentId, memberDetails);
    }

}
