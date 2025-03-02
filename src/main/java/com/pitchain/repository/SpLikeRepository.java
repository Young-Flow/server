package com.pitchain.repository;

import com.pitchain.entity.Member;
import com.pitchain.entity.Sp;
import com.pitchain.entity.SpLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpLikeRepository extends JpaRepository<SpLike, Long> {

    long countBySp(Sp sp);

    boolean existsByMemberAndSp(Member member, Sp sp);

    void deleteByMemberAndSp(Member member, Sp sp);
}
