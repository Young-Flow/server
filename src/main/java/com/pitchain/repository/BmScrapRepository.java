package com.pitchain.repository;

import com.pitchain.entity.Bm;
import com.pitchain.entity.BmScrap;
import com.pitchain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BmScrapRepository extends JpaRepository<BmScrap, Long> {
    long countByBm(Bm bm);

    boolean existsByMemberAndBm(Member member, Bm bm);

    void deleteByMemberAndBm(Member member, Bm bm);
}
