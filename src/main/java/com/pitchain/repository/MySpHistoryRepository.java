package com.pitchain.repository;

import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.MySpHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MySpHistoryRepository extends JpaRepository<MySpHistory, Long> {

    Optional<MySpHistory> findByMemberAndBm(Member member, Bm bm);
}
