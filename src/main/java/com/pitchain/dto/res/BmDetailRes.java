package com.pitchain.dto.res;

import com.pitchain.dto.BmWithScrapDto;
import com.pitchain.entity.Bm;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BmDetailRes(
        Long id,
        String name,
        String company,
        String intro,
        String mainCategory,
        List<String> subCategories,
        String logoImgURL,
        String description,
        String descImgURL,
        String address,
        LocalDateTime createdAt,
        String longPitchURL,
        String spURL,
        boolean isScraped,
        long scrapCnt,
        List<PtImgRes> ptImgResList
) {
    public static BmDetailRes createRes(BmWithScrapDto bmWithScrapDto, long scrapCnt, List<PtImgRes> ptImgResList, List<String> subCategories,
                                        String spURL, String logoImgURL, String descImgURL) {
        Bm bm = bmWithScrapDto.getBm();
        return BmDetailRes.builder()
                .id(bm.getId())
                .name(bm.getName())
                .company(bm.getCompany())
                .intro(bm.getIntro())
                .mainCategory(bm.getMainCategory().getKoreanName())
                .subCategories(subCategories)
                .logoImgURL(logoImgURL)
                .description(bm.getDescription())
                .descImgURL(descImgURL)
                .address(bm.getAddress())
                .createdAt(bm.getCreatedAt())
                .longPitchURL(bm.getLongPitchURL())
                .spURL(spURL)
                .isScraped(bmWithScrapDto.isScraped())
                .scrapCnt(scrapCnt)
                .ptImgResList(ptImgResList)
                .build();
    }
}
