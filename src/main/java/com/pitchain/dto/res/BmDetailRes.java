package com.pitchain.dto.res;

import com.pitchain.dto.BmWithScrapDto;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BmDetailRes(
        Long companyId,
        String companyProfileImgURL,
        String companyName,
        String companyAddress,

        Long bmId,
        String bmName,
        String intro,
        String mainCategory,
        List<String> subCategories,
        String description,
        String descImgURL,
        String bmAddress,
        LocalDateTime createdAt,
        String longPitchURL,
        boolean isScraped,
        long scrapCnt,
        List<String> spURLs,

        List<PtImgRes> ptImgResList
) {
    public static BmDetailRes createRes(
            BmWithScrapDto bmWithScrapDto, long scrapCnt, List<PtImgRes> ptImgResList, List<String> subCategories, String descImgURL, String profileImgURL, List<String> spURLs
    ) {
        Bm bm = bmWithScrapDto.getBm();
        Company company = bm.getCompany();
        Member member = company.getMember();

        return BmDetailRes.builder()
                .companyId(company.getId())
                .companyProfileImgURL(profileImgURL)
                .companyName(member.getName())
                .companyAddress(company.getAddress())

                .bmId(bm.getId())
                .bmName(bm.getName())
                .intro(bm.getIntro())
                .mainCategory(bm.getMainCategory().getKoreanName())
                .subCategories(subCategories)
                .description(bm.getDescription())
                .descImgURL(descImgURL)
                .bmAddress(bm.getAddress())
                .createdAt(bm.getCreatedAt())
                .longPitchURL(bm.getLongPitchURL())
                .isScraped(bmWithScrapDto.isScraped())
                .scrapCnt(scrapCnt)
                .spURLs(spURLs)

                .ptImgResList(ptImgResList)
                .build();
    }
}
