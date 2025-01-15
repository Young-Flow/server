package com.pitchain.dto.res;

import com.pitchain.dto.BmWithLikeDto;
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
        String subCategory,
        String logoImg,
        String description,
        String descriptionImg,
        String address,
        LocalDateTime createdAt,
        String longPitchUrl,
        String spURL,
        boolean isLiked,
        long likeCnt,
        List<PtImgRes> ptImgResList
) {
    public static BmDetailRes createRes(BmWithLikeDto bmWithLikeDto, long likeCnt, List<PtImgRes> ptImgResList) {
        Bm bm = bmWithLikeDto.getBm();
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
                .createdAt(bm.getCreatedAt())
                .longPitchUrl(bm.getLongPitchUrl())
                .spURL(bm.getShortPitchURL())
                .isLiked(bmWithLikeDto.isLiked())
                .likeCnt(likeCnt)
                .ptImgResList(ptImgResList)
                .build();
    }
}
