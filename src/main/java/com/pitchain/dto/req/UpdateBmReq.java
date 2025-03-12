package com.pitchain.dto.req;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.entity.Bm;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public record UpdateBmReq(
        String name,
        MainCategory mainCategory,
        List<SubCategory> subCategories,
        String intro,
        String description,
        String address,
        Long valuationCap,
        Long goalInvestment,
        Integer maxIssuedShare,
        LocalDate deadline,
        String longPitchURL
) {
    public Bm createBm(String descImgKey) {
        return Bm.builder()
                .name(name)
                .mainCategory(mainCategory)
                .intro(intro)
                .description(description)
                .descImgKey(descImgKey)
                .address(address)
                .valuationCap(valuationCap)
                .goalInvestment(goalInvestment)
                .maxIssuedShare(maxIssuedShare)
                .deadline(deadline)
                .longPitchURL(longPitchURL)
                .build();
    }
}
