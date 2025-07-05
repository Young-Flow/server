package com.pitchain.individual.application;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.individual.domain.Individual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IndividualRepository extends JpaRepository<Individual, Long> {
    Optional<Individual> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId);

    Optional<Individual> findByMemberId(Long memberId);
}
