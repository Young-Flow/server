package com.pitchain.dto.req;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateBmReq(
        String name,
        MainCategory mainCategory,
        @NotNull List<SubCategory> subCategories,
        String company,
        String intro,
        String description,
        String address,
        Long valuationCap,
        Long goalInvestment,
        Integer maxIssuedShare,
        LocalDate deadline,
        String longPitchURL
) {
    public Bm createBm(Member member, String descImgKey) {
        return Bm.builder()
                .member(member)
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
