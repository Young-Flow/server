package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.dto.BmWithScrapDto;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.dto.res.PtImgRes;
import com.pitchain.entity.*;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BmQueryService {
    private final EntityFacade entityFacade;
    private final BmRepository bmRepository;
    private final BmScrapService bmScrapService;
    private final PtImgService ptImgService;
    private final BmSubcategoryService bmSubcategoryService;
    private final SpService spService;
    private final MyBmHistoryService myBmHistoryService;

    @Transactional(readOnly = true)
    public BmDetailRes getBmDetail(MemberDetails memberDetails, Long bmId) {
        Member member = entityFacade.getMember(memberDetails.id());

        BmWithScrapDto bmWithScrapDto = bmRepository.getBmWithScrapDto(member.getId(), bmId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BM_NOT_FOUND));
        Bm bm = bmWithScrapDto.getBm();

        long scrapCnt = bmScrapService.countByBm(bm.getId());

        List<PtImg> ptImgs = ptImgService.getPtImgsByBmId(bm.getId());
        List<PtImgRes> ptImgResList = ptImgs.stream()
                .map(ptImg -> PtImgRes.createRes(ptImg.getSerialNum(), ptImg.getImgKey()))
                .toList();

        List<BmSubCategory> bmSubCategories = bmSubcategoryService.getBmSubCategoryByBmId(bmId);
        List<String> subcategoriesInKorean = convertToKoreanName(bmSubCategories);

        List<Sp> sps = spService.getSpsByBmId(bm.getId());
        List<String> spURLs = sps.stream()
                .map(Sp::getSpKey)
                .toList();

        myBmHistoryService.saveMyBmHistory(member, bm);

        return BmDetailRes.createRes(bmWithScrapDto, ptImgResList, scrapCnt, subcategoriesInKorean, spURLs);
    }

    private static List<String> convertToKoreanName(List<BmSubCategory> bmSubCategories) {
        List<String> subcategoriesInKorean = bmSubCategories.stream()
                .map(bmSubCategory -> bmSubCategory.getSubCategory().getKoreanName())
                .toList();
        return subcategoriesInKorean;
    }
}
