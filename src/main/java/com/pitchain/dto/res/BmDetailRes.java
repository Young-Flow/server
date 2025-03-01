package com.pitchain.dto.res;

import com.pitchain.dto.BmWithLikeDto;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Company;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BmDetailRes(
        Long id,
        String companyLogoImgURL,
        String companyName,
        String companyAddress,
        String name,
        String intro,
        String mainCategory,
        List<String> subCategories,
        String description,
        String descImgURL,
        String bmAddress,
        LocalDateTime createdAt,
        String longPitchURL,
        boolean isLiked,
        long likeCnt,
        List<String> spURLs,
        List<PtImgRes> ptImgResList
) {
    public static BmDetailRes createRes(Company company, String companyLogoImgURL, BmWithLikeDto bmWithLikeDto,
                                        String descImgURL, long likeCnt,
                                        List<String> subCategories, List<String> spURLs, List<PtImgRes> ptImgResList) {
        Bm bm = bmWithLikeDto.getBm();
        return BmDetailRes.builder()
                .id(bm.getId())
                .companyLogoImgURL(companyLogoImgURL)
                .companyName(company.getName())
                .companyAddress(company.getAddress())
                .name(bm.getName())
                .intro(bm.getIntro())
                .mainCategory(bm.getMainCategory().getKoreanName())
                .subCategories(subCategories)
                .description(bm.getDescription())
                .descImgURL(descImgURL)
                .bmAddress(bm.getAddress())
                .createdAt(bm.getCreatedAt())
                .longPitchURL(bm.getLongPitchURL())
                .isLiked(bmWithLikeDto.isLiked())
                .likeCnt(likeCnt)
                .spURLs(spURLs)
                .ptImgResList(ptImgResList)
                .build();
    }
}
