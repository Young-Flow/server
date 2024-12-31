package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.CommentDto;
import com.pitchain.dto.res.CommentRes;
import com.pitchain.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bms")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{bmId}/comments")
    public CustomApiResponse addComment(@PathVariable("bmId") Long bmId,
                                        @AuthenticationPrincipal Long memberId,
                                        @RequestBody CommentDto.addCommentDto dto) {
        commentService.addComment(bmId, memberId, dto);
        return CustomApiResponse.onSuccess();
    }

    @GetMapping("/{bmId}/comments")
    public CustomApiResponse<List<CommentRes>> getComments(@PathVariable("bmId") Long bmId) {
        List<CommentRes> comments = commentService.getComments(bmId);
        return CustomApiResponse.onSuccess(comments);
    }

    @PutMapping("/{bmId}/comments/{commentId}")
    public CustomApiResponse modifyComment(@PathVariable("bmId") Long bmId,
                                           @PathVariable("commentId") Long commentId,
                                           @AuthenticationPrincipal Long memberId,
                                           @RequestBody CommentDto.modifyCommentDto dto) {
        commentService.modifyComment(commentId, memberId, dto);
        return CustomApiResponse.onSuccess();
    }

    @DeleteMapping("/{bmId}/comments/{commentId}")
    public CustomApiResponse removeComment(@PathVariable("bmId") Long bmId,
                                           @PathVariable("commentId") Long commentId,
                                           @AuthenticationPrincipal Long memberId) {
        commentService.removeComment(commentId, memberId);
        return CustomApiResponse.onSuccess();
    }

}
