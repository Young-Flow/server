package com.pitchain.company.application;

import com.pitchain.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByMemberId(Long memberId);
}
