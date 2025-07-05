package com.pitchain.bm.presentation.req;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.SubCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record BmUpdateReq(
        @NotBlank(message = "이름은 필수입니다.")
        String name,
        @NotNull(message = "메인 카테고리는 필수입니다.")
        MainCategory mainCategory,
        @NotEmpty(message = "서브 카테고리는 필수입니다.")
        List<SubCategory> subCategories,
        @NotBlank(message = "소개는 필수입니다.")
        String intro,
        @NotBlank(message = "설명은 필수입니다.")
        String description,
        @NotBlank(message = "주소는 필수입니다.")
        String address,
        @NotNull(message = "평가 상한선은 필수입니다.")
        Long valuationCap,
        @NotNull(message = "목표 투자금은 필수입니다.")
        Long goalInvestment,
        @NotNull(message = "최대 발행 주식수는 필수입니다.")
        Integer maxIssuedShare,
        @NotNull(message = "마감일은 필수입니다.")
        LocalDate deadline,
        String longPitchURL
) {
}
