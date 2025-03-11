package com.pitchain.dto.res;

import com.pitchain.dto.SpWithLikeDto;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.entity.Sp;
import lombok.Builder;

import java.util.List;

@Builder
public record SpDetailRes(
        Long bmId,
        String bmName,
        String companyProfileImgURL,
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
    public static SpDetailRes createRes(SpWithLikeDto spWithLikeDto, long likeCnt, List<String> subCategories, String spURL, String thumbnailImgURL) {
        Sp sp = spWithLikeDto.getSp();
        Bm bm = sp.getBm();
        Company company = bm.getCompany();
        Member member = company.getMember();
        return SpDetailRes.builder()
                .bmId(sp.getBm().getId())
                .bmName(bm.getName())
                .companyProfileImgURL(member.getProfileImgKey())
                .companyName(member.getName())
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
