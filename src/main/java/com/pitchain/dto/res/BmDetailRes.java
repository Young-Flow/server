package com.pitchain.dto.res;

import com.pitchain.entity.Bm;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record BmDetailRes(
        Long id,
        String name,
        String company,
        String intro,
        String mainCategory,
        String subCategory,
        String logoImg,
        String description,
        String descriptionImg,
        String address,
        LocalDateTime createdAt,
        String longPitchUrl,
        String spURL,
        boolean isLiked // todo MyBm 개발 완료 후 추가 예정
) {
    public static BmDetailRes createRes(Bm bm) {
        return BmDetailRes.builder()
                .id(bm.getId())
                .name(bm.getName())
                .company(bm.getCompany())
                .intro(bm.getIntro())
                .mainCategory(bm.getMainCategory().getKoreanName())
                .subCategory(bm.getSubCategory().getKoreanName())
                .logoImg(bm.getLogoImg())
                .description(bm.getDescription())
                .descriptionImg(bm.getDescriptionImg())
                .address(bm.getAddress())
                .valuationCap(bm.getValuationCap())
                .deadline(bm.getDeadline())
                .createdAt(bm.getCreatedAt())
                .goalInvestment(bm.getGoalInvestment())
                .longPitchUrl(bm.getLongPitchUrl())
                .spURL(bm.getSp().getShortPitchURL())
                .build();
    }
}
