package com.pitchain.individual.application;

import com.pitchain.member.presentation.req.BaseMemberUpdateReq;
import com.pitchain.individual.presentation.req.IndividualUpdateReq;
import com.pitchain.individual.domain.Individual;
import com.pitchain.member.domain.Member;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.common.application.EntityFacade;
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
