package com.pitchain.individual.application;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.individual.domain.Individual;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IndividualQueryService {
    private final IndividualRepository individualRepository;

    @Transactional(readOnly = true)
    public Optional<Individual> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId) {
        return individualRepository.findByOauthProviderAndSocialId(oauthProvider, socialId);
    }
}
