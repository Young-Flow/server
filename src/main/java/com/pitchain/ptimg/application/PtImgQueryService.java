package com.pitchain.ptimg.application;

import com.pitchain.bm.domain.Bm;
import com.pitchain.ptimg.domain.PtImg;
import com.pitchain.common.application.EntityFacade;
import com.pitchain.ptimg.infrastructure.PtImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PtImgQueryService {
    private final EntityFacade entityFacade;
    private final PtImgRepository ptImgRepository;

    @Transactional(readOnly = true)
    public List<PtImg> getPtImgsByBmId(Long bmId) {
        Bm bm = entityFacade.getBm(bmId);

        return ptImgRepository.findAllByBmId(bm.getId());
    }
}
