package com.pitchain.repository;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralHandler;
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

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    public Bm getBm(Long bmId) {
        return bmRepository.findById(bmId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.BM_NOT_FOUND));
    }

    public Sp getSp(Long spId) {
        return spRepository.findById(spId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.SP_NOT_FOUND));
    }

    public Company getCompany(MemberDetails memberDetails) {
        if (memberDetails.memberRole().equals(MemberRole.INDIVIDUAL))
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);

        return companyRepository.findByMemberId(memberDetails.id())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.COMPANY_NOT_FOUND));
    }
}
