package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.req.CommentReq;
import com.pitchain.dto.res.BaseCommentRes;
import com.pitchain.dto.res.CommentRes;
import com.pitchain.dto.res.DeletedCommentRes;
import com.pitchain.dto.res.ReplyCommentRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Comment;
import com.pitchain.entity.Member;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.CommentRepository;
import com.pitchain.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NEVER;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class CommentServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private S3Service s3Service;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        bmRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void 댓글_등록_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        CommentReq.AddCommentReq addCommentReq = new CommentReq.AddCommentReq();
        String content = "댓글 내용";
        addCommentReq.setContent(content);

        //when
        commentService.addComment(bm.getId(), member.getId(), addCommentReq);

        //then
        List<Comment> comments = commentRepository.findByBm(bm);
        Comment comment = comments.get(0);
        assertThat(comment.getMember()).isEqualTo(member);
        assertThat(comment.getParentComment()).isNull();
        assertThat(comment.getChildComments().size()).isEqualTo(0);
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getBm()).isEqualTo(bm);
    }

    @Test
    @Transactional(propagation = NEVER)
    void 답글_등록_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        Comment parentComment = saveComment(member, bm);
        Long parentCommentId = parentComment.getId();

        CommentReq.AddCommentReq addReplyCommentReq = new CommentReq.AddCommentReq();
        String replyCommentContent = "답글 내용";
        addReplyCommentReq.setContent(replyCommentContent);
        addReplyCommentReq.setParentCommentId(parentCommentId);

        //when
        commentService.addComment(bm.getId(), member.getId(), addReplyCommentReq);

        //then
        List<Comment> comments = commentRepository.findByBm(bm);
        Comment comment = comments.get(0);

        List<Comment> replyComments = comment.getChildComments();
        Comment replyComment = replyComments.get(0);
        assertThat(replyComment.getParentComment()).isEqualTo(comment);
        assertThat(replyComment.getContent()).isEqualTo(replyCommentContent);
        assertThat(replyComment.getMember().getId()).isEqualTo(member.getId());
        assertThat(replyComment.getBm().getId()).isEqualTo(bm.getId());
    }

    @Test
    void 댓글_존재_조회_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);
        Comment comment = saveComment(member, bm);

        //when
        List<? extends BaseCommentRes> comments = commentService.getComments(bm.getId());

        //then
        CommentRes foundComment = (CommentRes) comments.get(0);
        assertThat(foundComment.getCommentId()).isEqualTo(comment.getId());
        assertThat(foundComment.getWriterId()).isEqualTo(member.getId());
        assertThat(foundComment.getContent()).isEqualTo(comment.getContent());
    }

    @Test
    void 댓글_미존재_조회_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        //when
        List<? extends BaseCommentRes> comments = commentService.getComments(bm.getId());

        //then
        assertThat(comments.size()).isEqualTo(0);
    }

    @Test
    @Transactional(propagation = NEVER)
    void 답글_포함_댓글_조회_성공() {
        //given
        Member memberA = saveMember();
        Bm bm = saveBm(memberA);

        Comment parentComment = saveComment(memberA, bm);

        Member memberB = saveMember();
        Comment replyComment = saveReplyComment(memberB, parentComment, bm);

        //when
        List<? extends BaseCommentRes> comments = commentService.getComments(bm.getId());

        //then
        CommentRes foundComment = (CommentRes) comments.get(0);
        assertThat(foundComment.getCommentId()).isEqualTo(parentComment.getId());
        assertThat(foundComment.getWriterId()).isEqualTo(parentComment.getMember().getId());
        assertThat(foundComment.getContent()).isEqualTo(parentComment.getContent());
        assertThat(foundComment.getWriterProfileImgURL()).isEqualTo(s3Service.getFileURL(parentComment.getMember().getProfileImgKey()));

        List<ReplyCommentRes> replyComments = foundComment.getReplyComments();
        ReplyCommentRes replyCommentRes = replyComments.get(0);
        assertThat(replyCommentRes.content()).isEqualTo(replyComment.getContent());
        assertThat(replyCommentRes.writerId()).isEqualTo(replyComment.getMember().getId());
        assertThat(replyCommentRes.writerProfileImgURL()).isEqualTo(s3Service.getFileURL(replyComment.getMember().getProfileImgKey()));
    }

    @Test
    @Transactional(propagation = NEVER)
    @DisplayName("답글이 있는 댓글은 삭제되어도 조회된다.")
    void 삭제된_댓글_조회_성공() {
        //given
        Member memberA = saveMember();
        Bm bm = saveBm(memberA);
        Comment parentComment = new Comment(memberA, bm, null, "댓글 내용");
        parentComment.deleteParentComment();
        commentRepository.save(parentComment);

        Member memberB = saveMember();
        Comment replyComment = saveReplyComment(memberB, parentComment, bm);

        //when
        List<? extends BaseCommentRes> comments = commentService.getComments(bm.getId());

        //then
        DeletedCommentRes deletedCommentRes = (DeletedCommentRes) comments.get(0);
        assertThat(deletedCommentRes.getCommentId()).isEqualTo(parentComment.getId());

        ReplyCommentRes replyCommentRes = deletedCommentRes.getReplyComments().get(0);
        assertThat(replyCommentRes.commentId()).isEqualTo(replyComment.getId());
        assertThat(replyCommentRes.writerId()).isEqualTo(replyComment.getMember().getId());
        assertThat(replyCommentRes.writerProfileImgURL()).isEqualTo(s3Service.getFileURL(replyComment.getMember().getProfileImgKey()));
    }

    @Test
    void 댓글_수정_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        Comment comment = saveComment(member, bm);
        Long commentId = comment.getId();

        CommentReq.ModifyCommentReq modifyCommentReq = new CommentReq.ModifyCommentReq();
        String modifiedContent = "변경된 댓글 내용";
        modifyCommentReq.setContent(modifiedContent);

        //when
        commentService.modifyComment(commentId, member.getId(), modifyCommentReq);

        //then
        Comment modifiedComment = commentRepository.findById(commentId).orElseThrow();
        assertThat(modifiedComment.getContent()).isEqualTo(modifiedContent);
        assertThat(modifiedComment.getBm()).isEqualTo(bm);
        assertThat(modifiedComment.getMember()).isEqualTo(member);
    }

    @Test
    void 댓글_수정_실패() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        Comment comment = saveComment(member, bm);
        Long commentId = comment.getId();

        Member invalidMember = saveMember();
        Long invalidId = Long.MAX_VALUE;

        CommentReq.ModifyCommentReq modifyCommentReq = new CommentReq.ModifyCommentReq();
        modifyCommentReq.setContent("댓글 내용");

        //when
        GeneralHandler error1 = Assertions.assertThrows(GeneralHandler.class, () -> commentService.modifyComment(commentId, invalidMember.getId(), modifyCommentReq));
        GeneralHandler error2 = Assertions.assertThrows(GeneralHandler.class, () -> commentService.modifyComment(invalidId, member.getId(), modifyCommentReq));
        GeneralHandler error3 = Assertions.assertThrows(GeneralHandler.class, () -> commentService.modifyComment(commentId, invalidId, modifyCommentReq));

        //then
        assertThat(error1.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
        assertThat(error2.getErrorStatus()).isEqualTo(ErrorStatus.COMMENT_NOT_FOUND);
        assertThat(error3.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("답글이 없는 댓글은 DB에서 삭제된다.")
    void 댓글_삭제_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        Comment comment = saveComment(member, bm);
        Long commentId = comment.getId();

        //when
        commentService.removeComment(commentId, member.getId());

        //then
        assertThat(commentRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional(propagation = NEVER)
    @DisplayName("답글이 있는 댓글은 DB에서 삭제되지 않는다.")
    void 답글_있는_댓글_삭제_성공() {
        //given
        Member memberA = saveMember();
        Bm bm = saveBm(memberA);
        Comment parentComment = saveComment(memberA, bm);

        Member memberB = saveMember();
        saveReplyComment(memberB, parentComment, bm);

        //when
        commentService.removeComment(parentComment.getId(), memberA.getId());

        //then
        Comment foundParentComment = commentRepository.findById(parentComment.getId()).orElseThrow();
        assertThat(foundParentComment.isDelYN()).isTrue();
    }

    @Test
    void 댓글_삭제_실패() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        Comment comment = saveComment(member, bm);
        Long commentId = comment.getId();

        Member invalidMember = saveMember();
        Long invalidId = Long.MAX_VALUE;

        //when
        GeneralHandler error1 = Assertions.assertThrows(GeneralHandler.class, () -> commentService.removeComment(commentId, invalidMember.getId()));
        GeneralHandler error2 = Assertions.assertThrows(GeneralHandler.class, () -> commentService.removeComment(invalidId, member.getId()));
        GeneralHandler error3 = Assertions.assertThrows(GeneralHandler.class, () -> commentService.removeComment(commentId, invalidId));

        //then
        assertThat(error1.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
        assertThat(error2.getErrorStatus()).isEqualTo(ErrorStatus.COMMENT_NOT_FOUND);
        assertThat(error3.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
    }

    private Member saveMember() {
        return memberRepository.save(new Member(Country.USA, "profileImg.jpg"));
    }

    private Bm saveBm(Member member) {
        return bmRepository.save(new Bm(member, "bmName", MainCategory.FOOD, "bmIntro", "bmDescription",
                "bmDescriptionImg", "companyAddress", 100000L, 1000L, 1000, LocalDate.now(), "longPitchUrl"));
    }

    private Comment saveComment(Member member, Bm bm) {
        return commentRepository.save(new Comment(member, bm, null, "댓글 내용"));
    }

    private Comment saveReplyComment(Member member, Comment parentComment, Bm bm) {
        return commentRepository.save(new Comment(member, bm, parentComment, "답글 내용"));
    }
}
