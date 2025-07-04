package com.pitchain.service;

import com.pitchain.entity.Sp;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.SpLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SpLikeQueryService {
    private final EntityFacade entityFacade;
    private final SpLikeRepository spLikeRepository;

    @Transactional(readOnly = true)
    public Long countBySpId(Long spId) {
        Sp sp = entityFacade.getSp(spId);
        return spLikeRepository.countBySp(sp);
    }
}
