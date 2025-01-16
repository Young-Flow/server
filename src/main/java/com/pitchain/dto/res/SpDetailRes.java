package com.pitchain.dto.res;

import com.pitchain.dto.SpWithLikeDto;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Sp;
import lombok.Builder;

@Builder
public record SpDetailRes(
        Long bmId,
        String shortPitchURL,
        String thumbnailImg,
        int views,
        String name,
        String logoImg,
        String mainCategory,
        String subCategory,
        String company,
        boolean isLiked,
        long likeCnt
) {
    public static SpDetailRes createRes(SpWithLikeDto spWithLikeDto, long likeCnt) {
        Sp sp = spWithLikeDto.getSp();
        Bm bm = sp.getBm();
        return SpDetailRes.builder()
                .bmId(sp.getBm().getId())
                .shortPitchURL(sp.getShortPitchURL())
                .thumbnailImg(sp.getThumbnailImg())
                .views(sp.getViews())
                .name(sp.getName())
                .logoImg(bm.getLogoImg())
                .mainCategory(bm.getMainCategory().getKoreanName())
                .subCategory(bm.getSubCategory().getKoreanName())
                .company(bm.getCompany())
                .isLiked(spWithLikeDto.isLiked())
                .likeCnt(likeCnt)
                .build();
    }
}
