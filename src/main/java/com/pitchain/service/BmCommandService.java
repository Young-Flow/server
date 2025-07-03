package com.pitchain.service;

import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.dto.req.BmCreateReq;
import com.pitchain.dto.req.BmUpdateReq;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Company;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.EntityFacade;
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

