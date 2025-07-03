package com.pitchain.service;

import com.pitchain.entity.Bm;
import com.pitchain.repository.BmScrapRepository;
import com.pitchain.repository.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BmScrapQueryService {

    private final EntityFacade entityFacade;
    private final BmScrapRepository bmScrapRepository;

    @Transactional(readOnly = true)
    public long countByBm(Long bmId) {
        Bm bm = entityFacade.getBm(bmId);
        return bmScrapRepository.countByBm(bm);
    }
}
