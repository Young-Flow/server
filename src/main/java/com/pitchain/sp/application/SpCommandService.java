package com.pitchain.sp.application;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.constant.SpStatus;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.sp.presentation.req.SpCreateReq;
import com.pitchain.bm.domain.Bm;
import com.pitchain.company.domain.Company;
import com.pitchain.sp.domain.Sp;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.common.application.EntityFacade;
import com.pitchain.sp.infrastucture.SpRepository;
import com.pitchain.upload.application.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class SpCommandService {
    private final EntityFacade entityFacade;
    private final SpRepository spRepository;
    private final S3Service s3Service;

    @Transactional
    public void createSp(MemberDetails memberDetails, Long bmId, SpCreateReq spCreateReq, MultipartFile thumbnailImg) {
        Company company = entityFacade.getCompany(memberDetails);
        Bm bm = entityFacade.getBm(bmId);

        validateBmOwner(company, bm);

        String thumbnailImgKey = s3Service.uploadFile(thumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL);

        Sp sp = Sp.of(bm, thumbnailImgKey, spCreateReq.name());
        spRepository.save(sp);
    }

    @Transactional
    public void updateSp(MemberDetails memberDetails, Long bmId, Long spId, String name, MultipartFile thumbnailImg) {
        Company company = entityFacade.getCompany(memberDetails);
        Bm bm = entityFacade.getBm(bmId);
        Sp sp = entityFacade.getSp(spId);

        validateSpOwner(company, sp);
        validateRelation(bm, sp);

        if (thumbnailImg != null) {
            s3Service.deleteImg(sp.getThumbnailImgKey());
            String thumbnailImgKey = s3Service.uploadFile(thumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL);
            sp.updateThumbnailImgKey(thumbnailImgKey);
        }

        sp.update(name);
    }

    @Transactional
    public void deleteSp(MemberDetails memberDetails, Long bmId, Long spId) {
        Company company = entityFacade.getCompany(memberDetails);
        Sp sp = entityFacade.getSp(spId);
        Bm bm = entityFacade.getBm(bmId);

        validateSpOwner(company, sp);
        validateRelation(bm, sp);

        s3Service.deleteImg(sp.getThumbnailImgKey());
        spRepository.delete(sp);
    }

    @Transactional
    public void updateStatus(Long spId, SpStatus spStatus) {
        Sp sp = entityFacade.getSp(spId);
        sp.updateStatus(spStatus);
    }

    private static void validateRelation(Bm bm, Sp sp) {
        if (!bm.isOwner(sp.getBm().getId())) {
            throw new GeneralException(ErrorStatus.COMPANY_FORBIDDEN);
        }
    }

    private static void validateSpOwner(Company company, Sp sp) {
        if (!company.isOwner(sp.getBm().getCompany().getId())) {
            throw new GeneralException(ErrorStatus.COMPANY_FORBIDDEN);
        }
    }

    private static void validateBmOwner(Company company, Bm bm) {
        if (!company.isOwner(bm.getCompany().getId())) {
            throw new GeneralException(ErrorStatus.COMPANY_FORBIDDEN);
        }
    }
}
