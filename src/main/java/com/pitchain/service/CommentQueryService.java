package com.pitchain.service;

import com.pitchain.dto.res.BaseCommentRes;
import com.pitchain.dto.res.CommentRes;
import com.pitchain.dto.res.DeletedCommentRes;
import com.pitchain.dto.res.ReplyCommentRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Comment;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.CommentRepository;
import com.pitchain.repository.CommentRepositoryCustom;
import com.pitchain.repository.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Service
public class CommentQueryService {

    private final EntityFacade entityFacade;
    private final CommentRepository commentRepository;
    private final CommentRepositoryCustom commentRepositoryCustom;

    @Transactional(readOnly = true)
    public List<? extends BaseCommentRes> getComments(Long bmId, MemberDetails memberDetails) {
        Bm bm = entityFacade.getBm(bmId);
        List<Comment> comments = commentRepository.findAllByBm(bm);
        List<Long> ids = comments.stream()
                .map(Comment::getId)
                .toList();

        //댓글별 답글 조회
        Map<Long, List<Comment>> replyCommentMap = commentRepositoryCustom.findByParentComment(ids).stream()
                .collect(groupingBy(comment -> comment.getParentComment().getId(), Collectors.toList()));

        return comments.stream()
                .map(comment -> generateCommentRes(comment, replyCommentMap))
                .toList();
    }

    private BaseCommentRes generateCommentRes(Comment comment,
                                              Map<Long, List<Comment>> replyCommentMap) {
        //댓글의 답글 조회
        List<ReplyCommentRes> replyCommentResList =
                Optional.ofNullable(replyCommentMap.get(comment.getId()))
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(replyComment -> ReplyCommentRes.createRes(
                                replyComment, replyComment.getMember().getProfileImgKey()
                        )).toList();

        if (comment.isDelYN()) {
            return DeletedCommentRes.createRes(comment, replyCommentResList);
        }

        return CommentRes.createRes(comment, replyCommentResList);
    }
}
