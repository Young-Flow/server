package com.pitchain.dto.res;

import com.pitchain.dto.SpWithLikeDto;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Sp;
import lombok.Builder;

import java.util.List;

@Builder
public record SpDetailRes(
        Long bmId,
        String spURL,
        String thumbnailImgURL,
        int views,
        String name,
        String logoImgURL,
        String mainCategory,
        List<String> subCategories,
        String company,
        boolean isLiked,
        long likeCnt
) {
    public static SpDetailRes createRes(SpWithLikeDto spWithLikeDto, String spURL, String thumbnailImgURL,
                                        long likeCnt, List<String> subCategories, String logoImgURL) {
        Sp sp = spWithLikeDto.getSp();
        Bm bm = sp.getBm();
        return SpDetailRes.builder()
                .bmId(sp.getBm().getId())
                .spURL(spURL)
                .thumbnailImgURL(thumbnailImgURL)
                .views(sp.getViews())
                .name(sp.getName())
                .logoImgURL(logoImgURL)
                .mainCategory(bm.getMainCategory().getKoreanName())
                .subCategories(subCategories)
                .company(bm.getCompany())
                .isLiked(spWithLikeDto.isLiked())
                .likeCnt(likeCnt)
                .build();
    }
}
