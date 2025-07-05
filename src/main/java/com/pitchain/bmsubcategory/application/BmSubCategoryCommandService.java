package com.pitchain.bmsubcategory.application;

import com.pitchain.common.constant.SubCategory;
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
public class BmSubCategoryCommandService {
    private final EntityFacade entityFacade;
    private final BmSubCategoryRepository bmSubCategoryRepository;

    @Transactional
    public void saveAll(Long bmId, List<SubCategory> subCategories) {
        Bm bm = entityFacade.getBm(bmId);

        saveBmSubCategories(subCategories, bm);
    }

    @Transactional
    public void update(Long bmId, List<SubCategory> subCategories) {
        Bm bm = entityFacade.getBm(bmId);

        bmSubCategoryRepository.deleteAllByBmId(bm.getId());

        saveBmSubCategories(subCategories, bm);
    }

    private void saveBmSubCategories(List<SubCategory> subCategories, Bm bm) {
        subCategories.forEach(subCategory -> {
            bmSubCategoryRepository.save(BmSubCategory.create(bm, subCategory));
        });
    }
}
