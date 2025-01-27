package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.service.BmPrefService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BmPrefController {

    private final BmPrefService bmPrefService;

    @PostMapping("/preferences")
    public CustomApiResponse createCategoryPref(@AuthenticationPrincipal Long memberId,
                                                @RequestBody  List<SubCategory> subCategories) {
        bmPrefService.createCategoryPref(memberId, subCategories);
        return CustomApiResponse.onSuccess();
    }
}

