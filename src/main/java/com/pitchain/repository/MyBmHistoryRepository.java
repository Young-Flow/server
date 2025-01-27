package com.pitchain.repository;

import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.MyBmHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyBmHistoryRepository extends JpaRepository<MyBmHistory, Long> {
    Optional<MyBmHistory> findByMemberAndBm(Member member, Bm bm);
}
