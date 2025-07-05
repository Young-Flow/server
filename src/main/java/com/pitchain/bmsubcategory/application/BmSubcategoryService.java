package com.pitchain.bmsubcategory.application;

import com.pitchain.common.constant.SubCategory;
import com.pitchain.bmsubcategory.domain.BmSubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BmSubcategoryService {
    private final BmSubCategoryCommandService bmSubCategoryCommandService;
    private final BmSubCategoryQueryService bmSubCategoryQueryService;

    @Transactional
    public void saveAll(Long bmId, List<SubCategory> subCategories) {
        bmSubCategoryCommandService.saveAll(bmId, subCategories);
    }

    @Transactional(readOnly = true)
    public List<BmSubCategory> getBmSubCategoryByBmId(Long bmId) {
        return bmSubCategoryQueryService.getBmSubCategoryByBmId(bmId);
    }

    @Transactional
    public void update(Long bmId, List<SubCategory> subCategories) {
        bmSubCategoryCommandService.update(bmId, subCategories);
    }
}
