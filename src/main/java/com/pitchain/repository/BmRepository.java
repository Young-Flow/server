package com.pitchain.repository;

import com.pitchain.dto.BmWithLikeDto;
import com.pitchain.entity.Bm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BmRepository extends JpaRepository<Bm, Long> {
    @Query("""
                SELECT DISTINCT new com.pitchain.dto.BmWithLikeDto(
                    b,
                    CASE WHEN mb.member.id IS NOT NULL THEN true ELSE false END,
                    COUNT(mb.id)
                )
                FROM Bm b
                LEFT JOIN MyBm mb ON mb.bm.id = b.id AND mb.member.id = :memberId
                JOIN FETCH b.ptImgs
                WHERE b.id = :bmId
            """)
    Optional<BmWithLikeDto> getBmWithLikeDto(Long memberId, Long bmId);
}
