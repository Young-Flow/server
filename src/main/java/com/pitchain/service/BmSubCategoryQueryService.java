package com.pitchain.service;

import com.pitchain.entity.Bm;
import com.pitchain.entity.BmSubCategory;
import com.pitchain.repository.BmSubCategoryRepository;
import com.pitchain.repository.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BmSubCategoryQueryService {
    private final EntityFacade entityFacade;
    private final BmSubCategoryRepository bmSubCategoryRepository;

    @Transactional(readOnly = true)
    public List<BmSubCategory> getBmSubCategoryByBmId(Long bmId) {
        Bm bm = entityFacade.getBm(bmId);

        return bmSubCategoryRepository.findAllByBmId(bm.getId());
    }
}
