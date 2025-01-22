package com.pitchain.dto.req;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.entity.Bm;

import java.time.LocalDate;
import java.util.List;

public record UpdateBmReq(
        String name,
        MainCategory mainCategory,
        List<SubCategory> subCategories,
        String company,
        String intro,
        String description,
        String address,
        Long valuationCap,
        Integer goalInvestment,
        Integer maxIssuedShare,
        LocalDate deadline,
        String longPitchUrl
) {
    public Bm createBm(String logoImg, String descriptionImg) {
        return Bm.builder()
                .name(name)
                .mainCategory(mainCategory)
                .company(company)
                .logoImg(logoImg)
                .intro(intro)
                .description(description)
                .descriptionImg(descriptionImg)
                .address(address)
                .valuationCap(valuationCap)
                .goalInvestment(goalInvestment)
                .maxIssuedShare(maxIssuedShare)
                .deadline(deadline)
                .longPitchUrl(longPitchUrl)
                .build();
    }
}
