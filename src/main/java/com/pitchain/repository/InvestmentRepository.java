package com.pitchain.repository;

import com.pitchain.dto.FundraisingStatusDto;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    @Query("SELECT new com.pitchain.dto.FundraisingStatusDto(SUM(i.amount), COUNT(i), MIN(i.amount), MAX(i.amount)) " +
            "FROM Investment i " +
            "WHERE i.bm = :bm ")
    FundraisingStatusDto findFundraisingStatusByBm(@Param("bm") Bm bm);

}
