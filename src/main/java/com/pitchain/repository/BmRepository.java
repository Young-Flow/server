package com.pitchain.repository;

import com.pitchain.common.constant.SubCategory;
import com.pitchain.dto.BmWithScrapDto;
import com.pitchain.entity.Bm;
import com.pitchain.entity.PtImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BmRepository extends JpaRepository<Bm, Long> {
    @Query("""
                SELECT DISTINCT new com.pitchain.dto.BmWithScrapDto(
                    b,
                    CASE WHEN bs.member.id IS NOT NULL THEN true ELSE false END
                )
                FROM Bm b
                LEFT JOIN BmScrap bs ON bs.bm.id = b.id AND bs.member.id = :memberId
                WHERE b.id = :bmId
                GROUP BY b, bs.member.id
            """)
    Optional<BmWithScrapDto> getBmWithScrapDto(Long memberId, Long bmId);

    @Query("""
                SELECT bsc.subCategory
                FROM BmSubCategory bsc
                WHERE bsc.bm.id = :bmId
            """)
    List<SubCategory> getSubCategoriesByBmId(Long bmId);

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
