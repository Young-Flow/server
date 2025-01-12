package com.pitchain.repository;

import com.pitchain.dto.SpWithLikeDto;
import com.pitchain.entity.Sp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpRepository extends JpaRepository<Sp, Long> {
    @Query("""
                SELECT DISTINCT new com.pitchain.dto.SpWithLikeDto(
                    s,
                    CASE WHEN mb.member.id IS NOT NULL THEN true ELSE false END,
                    COUNT(mb.id)
                )
                FROM Sp s
                LEFT JOIN FETCH s.bm b
                LEFT JOIN MyBm mb ON mb.bm.id = b.id AND mb.member.id = :memberId
            """)
    List<SpWithLikeDto> findAllWithLike(@Param("memberId") Long memberId);

    @Query("""
                SELECT DISTINCT new com.pitchain.dto.SpWithLikeDto(
                    s,
                    CASE WHEN mb.member.id IS NOT NULL THEN true ELSE false END,
                    COUNT(mb.id)
                )
                FROM Sp s
                LEFT JOIN FETCH s.bm b
                LEFT JOIN MyBm mb ON mb.bm.id = b.id AND mb.member.id = :memberId
                WHERE s.id = :spId
            """)
    Optional<SpWithLikeDto> findSpWithLike(@Param("memberId") Long memberId,
                                           @Param("spId") Long spId);
}
