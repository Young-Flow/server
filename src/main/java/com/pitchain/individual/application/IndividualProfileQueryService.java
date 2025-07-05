package com.pitchain.individual.application;

import com.pitchain.individual.domain.Individual;
import com.pitchain.member.domain.Member;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.common.application.EntityFacade;
import com.pitchain.member.application.res.BaseMemberProfileRes;
import com.pitchain.individual.application.res.IndividualProfileRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class IndividualProfileQueryService {
    private final EntityFacade entityFacade;

    @Transactional(readOnly = true)
    public BaseMemberProfileRes getMyProfile(MemberDetails memberDetails) {
        Individual individual = entityFacade.getIndividual(memberDetails);
        Member member = individual.getMember();
        return IndividualProfileRes.createRes(member, individual);
    }
}
