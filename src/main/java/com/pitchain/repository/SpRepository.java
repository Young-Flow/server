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
                    CASE WHEN sl.member.id IS NOT NULL THEN true ELSE false END
                )
                FROM Sp s
                LEFT JOIN SpLike sl ON sl.sp.id = s.id AND sl.member.id = :memberId
            """)
    List<SpWithLikeDto> findAllWithLike(@Param("memberId") Long memberId);

    @Query("""
                SELECT DISTINCT new com.pitchain.dto.SpWithLikeDto(
                    s,
                    CASE WHEN sl.member.id IS NOT NULL THEN true ELSE false END
                )
                FROM Sp s
                LEFT JOIN SpLike sl ON sl.sp.id = s.id AND sl.member.id = :memberId
                WHERE s.id = :spId
            """)
    Optional<SpWithLikeDto> findSpWithLike(@Param("memberId") Long memberId,
                                           @Param("spId") Long spId);

    @Query("""
                SELECT DISTINCT new com.pitchain.dto.SpWithLikeDto(
                    s,
                    CASE WHEN sl.member.id IS NOT NULL THEN true ELSE false END
                )
                FROM Sp s
                LEFT JOIN SpLike sl ON sl.sp.id = s.id AND sl.member.id = :memberId
                WHERE s.bm.id IN :bmIds
            """)
    List<SpWithLikeDto> getSpWithLikeDtoRecommendedFromAi(@Param("memberId") Long memberId,
                                                          @Param("bmIds") List<Long> bmIds);


    List<Sp> findAllByBmId(Long bmId);

    @Query("SELECT s FROM Sp s " +
            "JOIN FETCH s.bm b " +
            "JOIN FETCH b.company c " +
            "JOIN FETCH c.member m " +
            "WHERE s.id = :spId")
    Optional<Sp> findSpWithAll(Long spId);
}
