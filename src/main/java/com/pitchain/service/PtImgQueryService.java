package com.pitchain.service;

import com.pitchain.entity.Bm;
import com.pitchain.entity.PtImg;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.PtImgRepository;
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
