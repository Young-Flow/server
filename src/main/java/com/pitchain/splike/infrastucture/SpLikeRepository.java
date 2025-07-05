package com.pitchain.splike.infrastucture;

import com.pitchain.member.domain.Member;
import com.pitchain.sp.domain.Sp;
import com.pitchain.splike.domain.SpLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpLikeRepository extends JpaRepository<SpLike, Long> {

    long countBySp(Sp sp);

    boolean existsByMemberAndSp(Member member, Sp sp);

    void deleteByMemberAndSp(Member member, Sp sp);
}
