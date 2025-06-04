package com.pitchain.service;

import com.pitchain.common.constant.SubCategory;
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
public class BmSubcategoryService {
    private final EntityFacade entityFacade;
    private final BmSubCategoryRepository bmSubCategoryRepository;

    @Transactional
    public void saveAll(Long bmId, List<SubCategory> subCategories) {
        Bm bm = entityFacade.getBm(bmId);

        saveBmSubCategories(subCategories, bm);
    }

    @Transactional
    public void saveBmSubCategories(List<SubCategory> subCategories, Bm bm) {
        subCategories.forEach(subCategory -> {
            bmSubCategoryRepository.save(BmSubCategory.create(bm, subCategory));
        });
    }

    public List<BmSubCategory> getBmSubCategoryByBmId(Long bmId) {
        Bm bm = entityFacade.getBm(bmId);

        return bmSubCategoryRepository.findAllByBmId(bm.getId());
    }

    @Transactional
    public void update(Long bmId, List<SubCategory> subCategories) {
        Bm bm = entityFacade.getBm(bmId);

        bmSubCategoryRepository.deleteAllByBmId(bm.getId());

        saveBmSubCategories(subCategories, bm);
    }
}
