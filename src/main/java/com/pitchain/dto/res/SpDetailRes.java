package com.pitchain.dto.res;

import com.pitchain.dto.SpWithLikeDto;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Company;
import com.pitchain.entity.Sp;
import lombok.Builder;

import java.util.List;

@Builder
public record SpDetailRes(
        Long bmId,
        String bmName,
        String companyLogoImgURL,
        String companyName,
        String companyAddress,
        String spURL,
        String thumbnailImgURL,
        int views,
        String name,
        String mainCategory,
        List<String> subCategories,
        boolean isLiked,
        long likeCnt
) {
    public static SpDetailRes createRes(Company company, String companyLogoImgURL, SpWithLikeDto spWithLikeDto, String spURL, String thumbnailImgURL,
                                        long likeCnt, List<String> subCategories) {
        Sp sp = spWithLikeDto.getSp();
        Bm bm = sp.getBm();
        return SpDetailRes.builder()
                .bmId(sp.getBm().getId())
                .bmName(bm.getName())
                .companyLogoImgURL(companyLogoImgURL)
                .companyName(company.getName())
                .companyAddress(company.getAddress())
                .spURL(spURL)
                .thumbnailImgURL(thumbnailImgURL)
                .views(sp.getViews())
                .name(sp.getName())
                .mainCategory(bm.getMainCategory().getKoreanName())
                .subCategories(subCategories)
                .isLiked(spWithLikeDto.isLiked())
                .likeCnt(likeCnt)
                .build();
    }
}
