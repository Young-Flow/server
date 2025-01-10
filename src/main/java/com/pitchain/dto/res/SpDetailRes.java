package com.pitchain.dto.res;

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
        String mainCategory,
        String subCategory,
        String company
) {
    public static SpDetailRes createRes(Sp sp) {
        Bm bm = sp.getBm();
        return SpDetailRes.builder()
                .bmId(sp.getBm().getId())
                .shortPitchURL(sp.getShortPitchURL())
                .thumbnailImg(sp.getThumbnailImg())
                .views(sp.getViews())
                .name(sp.getName())
                .mainCategory(bm.getMainCategory().getKoreanName())
                .subCategory(bm.getSubCategory().getKoreanName())
                .company(bm.getCompany())
                .build();
    }
}
