package com.pitchain.investment.infrastucture;

import com.pitchain.investment.infrastucture.dto.InvestmentStatusDto;
import com.pitchain.bm.domain.Bm;
import com.pitchain.investment.domain.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    @Query("SELECT new com.pitchain.investment.infrastucture.dto.InvestmentStatusDto(SUM(i.amount), COUNT(i), MIN(i.amount), MAX(i.amount)) " +
            "FROM Investment i " +
            "WHERE i.bm = :bm ")
    InvestmentStatusDto findInvestmentStatusByBm(@Param("bm") Bm bm);

}
