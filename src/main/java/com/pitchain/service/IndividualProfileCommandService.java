package com.pitchain.service;

import com.pitchain.dto.req.BaseMemberUpdateReq;
import com.pitchain.dto.req.IndividualUpdateReq;
import com.pitchain.entity.Individual;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class IndividualProfileCommandService {
    private final EntityFacade entityFacade;

    @Transactional
    public void updateMyProfile(MemberDetails memberDetails, BaseMemberUpdateReq req) {
        Individual individual = entityFacade.getIndividual(memberDetails);
        Member member = individual.getMember();
        member.updateProfile(((IndividualUpdateReq) req).getName());
    }
}
