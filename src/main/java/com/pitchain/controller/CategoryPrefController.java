package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.service.CategoryPrefService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/bms")
@RestController
public class CategoryPrefController {

    private final CategoryPrefService categoryPrefService;

    public CustomApiResponse createCategoryPref(@AuthenticationPrincipal Long memberId,
                                                List<SubCategory> subCategories) {
        categoryPrefService.createCategoryPref(memberId, subCategories);
        return CustomApiResponse.onSuccess();
    }
}

