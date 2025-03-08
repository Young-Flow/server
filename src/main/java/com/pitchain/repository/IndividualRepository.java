package com.pitchain.repository;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.entity.Individual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IndividualRepository extends JpaRepository<Individual, Long> {
    Optional<Individual> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId);
}
