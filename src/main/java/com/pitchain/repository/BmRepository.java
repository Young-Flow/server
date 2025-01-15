package com.pitchain.repository;

import com.pitchain.dto.BmWithLikeDto;
import com.pitchain.entity.Bm;
import com.pitchain.entity.PtImg;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BmRepository extends JpaRepository<Bm, Long> {
    @Query("""
                SELECT DISTINCT new com.pitchain.dto.BmWithLikeDto(
                    b,
                    CASE WHEN mb.member.id IS NOT NULL THEN true ELSE false END
                )
                FROM Bm b
                LEFT JOIN MyBm mb ON mb.bm.id = b.id AND mb.member.id = :memberId
                WHERE b.id = :bmId
                GROUP BY b, mb.member.id
            """)
    Optional<BmWithLikeDto> getBmWithLikeDto(Long memberId, Long bmId);

    @Query("""
                SELECT pi
                FROM PtImg pi
                WHERE pi.bm.id = :bmId
            """)
    List<PtImg> getPtImgsByBmId(Long bmId);

    @Query("""
                SELECT b 
                FROM Bm b
                LEFT JOIN FETCH b.ptImgs
                WHERE b.id = :bmId
            """)
    Optional<Bm> getByIdWithPtImgs(Long bmId);
}
