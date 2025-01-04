package com.pitchain.repository;

import com.pitchain.entity.Bm;
import com.pitchain.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    @Query("SELECT i FROM Investment i " +
            "WHERE i.bm = :bm " +
            "ORDER BY i.amount")
    List<Investment> findByBm(@Param("bm") Bm bm);

}
