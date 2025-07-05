package com.pitchain.ptimg.infrastructure;

import com.pitchain.ptimg.domain.PtImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PtImgRepository extends JpaRepository<PtImg, Long> {
    List<PtImg> findAllByBmId(Long bmId);
}
