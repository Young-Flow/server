package com.pitchain.mysphistory.infrastucture;

import com.pitchain.bm.domain.Bm;
import com.pitchain.member.domain.Member;
import com.pitchain.mysphistory.domain.MySpHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MySpHistoryRepository extends JpaRepository<MySpHistory, Long> {

    Optional<MySpHistory> findByMemberAndBm(Member member, Bm bm);
}
