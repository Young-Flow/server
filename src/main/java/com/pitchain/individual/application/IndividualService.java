package com.pitchain.individual.application;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.individual.domain.Individual;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IndividualService {
    private final IndividualCommandService individualCommandService;
    private final IndividualQueryService individualQueryService;

    @Transactional(readOnly = true)
    public Optional<Individual> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId) {
        return individualQueryService.findByOauthProviderAndSocialId(oauthProvider, socialId);
    }

    @Transactional
    public Individual saveIndividual(Long memberId, String socialId, OauthProvider oauthProvider) {
        return individualCommandService.saveIndividual(memberId, socialId, oauthProvider);
    }
}
