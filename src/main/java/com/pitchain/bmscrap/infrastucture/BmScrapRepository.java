package com.pitchain.bmscrap.infrastucture;

import com.pitchain.bm.domain.Bm;
import com.pitchain.bmscrap.domain.BmScrap;
import com.pitchain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BmScrapRepository extends JpaRepository<BmScrap, Long> {
    long countByBm(Bm bm);

    boolean existsByMemberAndBm(Member member, Bm bm);

    void deleteByMemberAndBm(Member member, Bm bm);
}
