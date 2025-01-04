package com.pitchain.repository;

import com.pitchain.entity.Sp;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpRepository extends JpaRepository<Sp, Long> {
    @EntityGraph(attributePaths = "bm")
    List<Sp> findAll();

    @EntityGraph(attributePaths = "bm")
    Optional<Sp> findById(Long spId);
}
