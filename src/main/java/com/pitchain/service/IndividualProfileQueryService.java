package com.pitchain.service;

import com.pitchain.dto.res.BaseMemberProfileRes;
import com.pitchain.dto.res.IndividualProfileRes;
import com.pitchain.entity.Individual;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.EntityFacade;
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
