package com.pitchain.mybmhistory.infrastructure;

import com.pitchain.bm.domain.Bm;
import com.pitchain.member.domain.Member;
import com.pitchain.mybmhistory.domain.MyBmHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyBmHistoryRepository extends JpaRepository<MyBmHistory, Long> {
    boolean existsByMemberAndBm(Member member, Bm bm);
}
