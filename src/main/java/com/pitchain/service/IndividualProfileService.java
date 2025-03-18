package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.req.BaseUpdateMemberReq;
import com.pitchain.dto.req.UpdateIndividualReq;
import com.pitchain.dto.res.BaseMemberProfileRes;
import com.pitchain.dto.res.IndividualProfileRes;
import com.pitchain.entity.Individual;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.IndividualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndividualProfileService implements MemberProfileService {

    private final IndividualRepository individualRepository;

    @Override
    public BaseMemberProfileRes getMyProfile(MemberDetails memberDetails) {
        Individual individual = getIndividual(memberDetails);
        Member member = individual.getMember();
        return IndividualProfileRes.createRes(member, individual);
    }

    @Override
    @Transactional
    public void updateMyProfile(MemberDetails memberDetails, BaseUpdateMemberReq req) {
        Individual individual = getIndividual(memberDetails);
        Member member = individual.getMember();
        member.updateProfile(((UpdateIndividualReq) req).getName());
    }

    private Individual getIndividual(MemberDetails memberDetails) {
        if (memberDetails.memberRole().equals(MemberRole.COMPANY)) {
            throw new GeneralHandler(ErrorStatus.COMPANY_FORBIDDEN);
        }

        return individualRepository.findByMemberId(memberDetails.id())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.INDIVIDUAL_NOT_FOUND));
    }
}
