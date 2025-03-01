package com.pitchain.repository;

import com.pitchain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<List<Company>> findByMemberId(Long id);
}
