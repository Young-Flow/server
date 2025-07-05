package com.pitchain.individual.application;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.individual.domain.Individual;
import com.pitchain.member.domain.Member;
import com.pitchain.common.application.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class IndividualCommandService {
    private final IndividualRepository individualRepository;
    private final EntityFacade entityFacade;

    @Transactional
    public Individual saveIndividual(Long memberId, String socialId, OauthProvider oauthProvider) {
        Member member = entityFacade.getMember(memberId);
        Individual individual = Individual.of(member, socialId, oauthProvider);
        return individualRepository.save(individual);
    }
}
