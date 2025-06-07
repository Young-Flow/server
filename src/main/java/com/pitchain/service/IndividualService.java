package com.pitchain.service;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.entity.Individual;
import com.pitchain.entity.Member;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.IndividualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IndividualService {
    private final IndividualRepository individualRepository;
    private final EntityFacade entityFacade;

    @Transactional(readOnly = true)
    public Optional<Individual> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId) {
        return individualRepository.findByOauthProviderAndSocialId(oauthProvider, socialId);
    }

    @Transactional
    public void saveIndividual(Long memberId, String socialId, OauthProvider oauthProvider) {
        Member member = entityFacade.getMember(memberId);
        Individual individual = Individual.of(member, socialId, oauthProvider);
        individualRepository.save(individual);
    }
}
