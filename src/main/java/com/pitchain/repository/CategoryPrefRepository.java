package com.pitchain.repository;

import com.pitchain.entity.CategoryPref;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryPrefRepository extends JpaRepository<CategoryPref, Long> {
    Optional<List<CategoryPref>> findAllByMemberId(Long memberId);
}
