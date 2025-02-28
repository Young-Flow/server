package com.pitchain.repository;

import com.pitchain.entity.Member;
import com.pitchain.entity.MySp;
import com.pitchain.entity.Sp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySpRepository extends JpaRepository<MySp, Long> {

    long countBySp(Sp sp);

    boolean existsByMemberAndSp(Member member, Sp sp);

    void deleteByMemberAndSp(Member member, Sp sp);
}
