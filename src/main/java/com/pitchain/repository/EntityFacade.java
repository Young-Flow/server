package com.pitchain.repository;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.entity.*;
import com.pitchain.jwt.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EntityFacade {
    private final MemberRepository memberRepository;
    private final BmRepository bmRepository;
    private final SpRepository spRepository;
    private final CompanyRepository companyRepository;
    private final CommentRepository commentRepository;
    private final IndividualRepository individualRepository;

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    public Bm getBm(Long bmId) {
        return bmRepository.findById(bmId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BM_NOT_FOUND));
    }

    public Sp getSp(Long spId) {
        return spRepository.findById(spId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SP_NOT_FOUND));
    }

    public Company getCompany(MemberDetails memberDetails) {
        if (memberDetails.memberRole().equals(MemberRole.INDIVIDUAL))
            throw new GeneralException(ErrorStatus.MEMBER_FORBIDDEN);

        return companyRepository.findByMemberId(memberDetails.id())
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMPANY_NOT_FOUND));
    }

    public Individual getIndividual(MemberDetails memberDetails) {
        if (memberDetails.memberRole().equals(MemberRole.COMPANY))
            throw new GeneralException(ErrorStatus.MEMBER_FORBIDDEN);

        return individualRepository.findByMemberId(memberDetails.id())
                .orElseThrow(() -> new GeneralException(ErrorStatus.INDIVIDUAL_NOT_FOUND));
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
    }
}
