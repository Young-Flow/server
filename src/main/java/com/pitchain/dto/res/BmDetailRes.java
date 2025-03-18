package com.pitchain.dto.res;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pitchain.common.converter.S3UrlSerializer;
import com.pitchain.dto.BmWithScrapDto;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.entity.Sp;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BmDetailRes(
        @NotNull
        Long companyId,
        @NotEmpty
        @JsonSerialize(using = S3UrlSerializer.class)
        String companyProfileImgURL,
        @NotEmpty
        String companyName,
        @NotEmpty
        String companyAddress,

        @NotNull
        Long bmId,
        @NotEmpty
        String bmName,
        @NotEmpty
        String intro,
        @NotEmpty
        String mainCategory,
        @NotEmpty
        List<String> subCategories,
        @NotEmpty
        String description,
        @NotEmpty
        @JsonSerialize(using = S3UrlSerializer.class)
        String descImgURL,
        @NotEmpty
        String bmAddress,
        @Past @NotNull
        LocalDateTime createdAt,
        @NotEmpty
        String longPitchURL,
        @NotNull
        Boolean isScraped,
        @NotNull
        Long scrapCnt,
        List<String> spURLs,

        List<PtImgRes> ptImgResList
) {
    public static BmDetailRes createRes(BmWithScrapDto bmWithScrapDto, List<PtImgRes> ptImgResList, long scrapCnt, List<String> subCategories) {
        Bm bm = bmWithScrapDto.getBm();
        Company company = bm.getCompany();
        Member member = company.getMember();

        return BmDetailRes.builder()
                .companyId(company.getId())
                .companyProfileImgURL(member.getProfileImgKey())  //추후에 JSON 직렬화 처리됨
                .companyName(member.getName())
                .companyAddress(company.getAddress())

                .bmId(bm.getId())
                .bmName(bm.getName())
                .intro(bm.getIntro())
                .mainCategory(bm.getMainCategory().getKoreanName())
                .subCategories(subCategories)
                .description(bm.getDescription())
                .descImgURL(bm.getDescImgKey())  //추후에 JSON 직렬화 처리됨
                .bmAddress(bm.getAddress())
                .createdAt(bm.getCreatedAt())
                .longPitchURL(bm.getLongPitchURL())
                .isScraped(bmWithScrapDto.isScraped())
                .scrapCnt(scrapCnt)
                .spURLs(bm.getSps().stream().map(Sp::getSpKey).toList())

                .ptImgResList(ptImgResList)
                .build();
    }
}
