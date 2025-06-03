package com.pitchain.controller;

import com.pitchain.jwt.MemberDetails;
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
    public void createCategoryPref(@AuthenticationPrincipal MemberDetails memberDetails,
                                   @RequestBody List<String> subCategoriesInKorean) {
        bmPrefService.createCategoryPref(memberDetails, subCategoriesInKorean);
    }
}

