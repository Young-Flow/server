package com.pitchain.dto.res;

import com.pitchain.common.constant.BmCategory;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Sp;
import lombok.Builder;

@Builder
public record SpRes(
        Long bmId,
        String shortPitchURL,
        String thumbnailImg,
        int views,
        String name,
        BmCategory category,
        String company
) {
    public static SpRes createRes(Sp sp) {
        Bm bm = sp.getBm();
        return SpRes.builder()
                .bmId(sp.getBm().getId())
                .shortPitchURL(sp.getShortPitchURL())
                .thumbnailImg(sp.getThumbnailImg())
                .views(sp.getViews())
                .name(sp.getName())
                .category(bm.getCategory())
                .company(bm.getCompany())
                .build();
    }
}
