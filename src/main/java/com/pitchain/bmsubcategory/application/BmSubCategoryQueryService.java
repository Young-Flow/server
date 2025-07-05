package com.pitchain.bmsubcategory.application;

import com.pitchain.bm.domain.Bm;
import com.pitchain.bmsubcategory.domain.BmSubCategory;
import com.pitchain.bmsubcategory.infrastructure.BmSubCategoryRepository;
import com.pitchain.common.application.EntityFacade;
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
