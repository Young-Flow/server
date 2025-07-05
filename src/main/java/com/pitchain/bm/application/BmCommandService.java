package com.pitchain.bm.application;

import com.pitchain.bm.domain.Bm;
import com.pitchain.bmscrap.application.BmScrapService;
import com.pitchain.bmsubcategory.application.BmSubcategoryService;
import com.pitchain.bmsubcategory.domain.BmSubCategory;
import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.bm.infrastucture.dto.BmWithScrapDto;
import com.pitchain.bm.presentation.req.BmCreateReq;
import com.pitchain.bm.presentation.req.BmUpdateReq;
import com.pitchain.bm.application.res.BmDetailRes;
import com.pitchain.ptimg.application.res.PtImgRes;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.company.domain.Company;
import com.pitchain.member.domain.Member;
import com.pitchain.mybmhistory.application.MyBmHistoryService;
import com.pitchain.ptimg.domain.PtImg;
import com.pitchain.ptimg.application.PtImgService;
import com.pitchain.bm.infrastucture.BmRepository;
import com.pitchain.common.application.EntityFacade;
import com.pitchain.sp.application.SpService;
import com.pitchain.upload.application.S3Service;
import com.pitchain.sp.domain.Sp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BmCommandService {
    private final BmRepository bmRepository;
    private final EntityFacade entityFacade;
    private final BmSubcategoryService bmSubcategoryService;
    private final PtImgService ptImgService;
    private final BmScrapService bmScrapService;
    private final SpService spService;
    private final MyBmHistoryService myBmHistoryService;
    private final S3Service s3Service;

    @Transactional
    public void createBm(MemberDetails memberDetails, BmCreateReq bmCreateReq, MultipartFile descImg) {
        Company company = entityFacade.getCompany(memberDetails);
        String descImgKey = s3Service.uploadFile(descImg, S3UploadTarget.COMPANY_DESC);
        Bm newBm = bmCreateReq.createBm(company, descImgKey);
        bmRepository.save(newBm);
        bmSubcategoryService.saveAll(newBm.getId(), bmCreateReq.subCategories());
    }

    @Transactional
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

    @Transactional
    public void updateBm(MemberDetails memberDetails, Long bmId, BmUpdateReq bmUpdateReq, MultipartFile descImg) {
        Company company = entityFacade.getCompany(memberDetails);
        Bm bm = entityFacade.getBm(bmId);
        validateBmOwner(bm, company);
        String descImgKey = s3Service.uploadFile(descImg, S3UploadTarget.COMPANY_DESC);
        bmSubcategoryService.update(bm.getId(), bmUpdateReq.subCategories());
        bm.update(
                bmUpdateReq.name(),
                bmUpdateReq.mainCategory(),
                bmUpdateReq.intro(),
                bmUpdateReq.description(),
                descImgKey,
                bmUpdateReq.address(),
                bmUpdateReq.valuationCap(),
                bmUpdateReq.goalInvestment(),
                bmUpdateReq.maxIssuedShare(),
                bmUpdateReq.deadline(),
                bmUpdateReq.longPitchURL()
        );
    }

    @Transactional
    public void updatePtImgs(MemberDetails memberDetails, Long bmId, List<String> uploadPtImgKeys) {
        Company company = entityFacade.getCompany(memberDetails);
        Bm bm = bmRepository.getByIdWithPtImgs(bmId)
                .orElseThrow(() -> new GeneralException(com.pitchain.common.apiPayload.ErrorStatus.BM_NOT_FOUND));
        validateBmOwner(bm, company);
        ptImgService.update(bm.getId(), uploadPtImgKeys);
    }

    @Transactional
    public void deleteBm(MemberDetails memberDetails, Long bmId) {
        Company company = entityFacade.getCompany(memberDetails);
        Bm bm = entityFacade.getBm(bmId);
        validateBmOwner(bm, company);
        bmRepository.delete(bm);
    }

    private static void validateBmOwner(Bm bm, Company company) {
        if (!company.isOwner(bm.getCompany().getId()))
            throw new GeneralException(com.pitchain.common.apiPayload.ErrorStatus.COMPANY_FORBIDDEN);
    }
}

