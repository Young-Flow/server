package com.pitchain.dto.req;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateBmReq(
        Long companyId,
        String name,
        MainCategory mainCategory,
        @NotNull List<SubCategory> subCategories,
        String intro,
        String description,
        String address,
        Long valuationCap,
        Long goalInvestment,
        Integer maxIssuedShare,
        LocalDate deadline,
        String longPitchURL
) {
    public Bm createBm(Company company, String descImgKey) {
        return Bm.builder()
                .company(company)
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
