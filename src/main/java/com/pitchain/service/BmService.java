package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.dto.BmWithScrapDto;
import com.pitchain.dto.req.BmCreateReq;
import com.pitchain.dto.req.BmUpdateReq;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.dto.res.PtImgRes;
import com.pitchain.entity.*;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.BmScrapRepository;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.MyBmHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class BmService {
    private final EntityFacade entityFacade;
    private final BmRepository bmRepository;
    private final BmScrapRepository bmScrapRepository;
    private final MyBmHistoryRepository myBmHistoryRepository;
    private final S3Service s3Service;

    public void createBm(MemberDetails memberDetails, BmCreateReq bmCreateReq, MultipartFile descImg) {
        Company company = entityFacade.getCompany(memberDetails);

        String descImgKey = s3Service.uploadFile(descImg, S3UploadTarget.COMPANY_DESC);

        Bm newBm = createBmReq.createBm(company, descImgKey);
        newBm.addSubCategories(createBmReq.subCategories());

        bmRepository.save(newBm);
    }

    public BmDetailRes getBmDetail(MemberDetails memberDetails, Long bmId) {
        Member member = entityFacade.getMember(memberDetails.id());

        BmWithScrapDto bmWithScrapDto = bmRepository.getBmWithScrapDto(member.getId(), bmId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BM_NOT_FOUND));
        Bm bm = bmWithScrapDto.getBm();

        long scrapCnt = bmScrapRepository.countByBm(bm);
        List<PtImgRes> ptImgResList = getPtImgResList(bmId);
        List<String> subCategories = bm.getKoreanSubCategories();

        myBmHistoryRepository.findByMemberAndBm(member, bm)
                .orElseGet(() -> myBmHistoryRepository.save(new MyBmHistory(member, bm)));

        return BmDetailRes.createRes(bmWithScrapDto, ptImgResList, scrapCnt, subCategories);
    }

    public void updateBm(MemberDetails memberDetails, Long bmId, BmUpdateReq bmUpdateReq, MultipartFile descImg) {
        Company company = entityFacade.getCompany(memberDetails);
        Bm bm = entityFacade.getBm(bmId);

        validateBmOwner(bm, company);

        String descImgKey = s3Service.uploadFile(descImg, S3UploadTarget.COMPANY_DESC);

        bm.updateSubCategories(bmUpdateReq.subCategories());

        Bm updateBm = bmUpdateReq.createBm(descImgKey);
        bm.update(updateBm);
    }

    public void updatePtImgs(MemberDetails memberDetails, Long bmId, List<String> uploadPtImgKeys) {
        Company company = entityFacade.getCompany(memberDetails);
        Bm bm = bmRepository.getByIdWithPtImgs(bmId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BM_NOT_FOUND));

        validateBmOwner(bm, company);

        deletePtImgs(bm);
        List<PtImg> uploadedPtImgs = uploadPtImgs(uploadPtImgKeys, bm);

        bm.updatePtImgs(uploadedPtImgs);
    }

    public void deleteBm(MemberDetails memberDetails, Long bmId) {
        Company company = entityFacade.getCompany(memberDetails);
        Bm bm = entityFacade.getBm(bmId);

        validateBmOwner(bm, company);

        bmRepository.delete(bm);
    }

    private List<PtImgRes> getPtImgResList(Long bmId) {
        List<PtImg> ptImgs = bmRepository.getPtImgsByBmId(bmId);
        List<PtImgRes> ptImgResList = ptImgs.stream()
                .map(ptImg -> PtImgRes.createRes(ptImg.getSerialNum(), ptImg.getImgKey()))
                .toList();
        return ptImgResList;
    }

    private void deletePtImgs(Bm bm) {
        List<PtImg> ptImgs = bm.getPtImgs();
        ptImgs.forEach(pi -> s3Service.deleteImg(pi.getImgKey()));
    }

    private List<PtImg> uploadPtImgs(List<String> ptImgKeys, Bm bm) {
        List<PtImg> uploadPtImgs = new ArrayList<>();
        for (int serialNum = 0; ptImgKeys != null && serialNum < ptImgKeys.size(); serialNum++) {
            String uploadFileKey = ptImgKeys.get(serialNum);
            PtImg ptImg = new PtImg(bm, serialNum, uploadFileKey);
            uploadPtImgs.add(ptImg);
        }
        return uploadPtImgs;
    }

    private static void validateBmOwner(Bm bm, Company company) {
        if (!bm.isOwner(company.getId()))
            throw new GeneralException(ErrorStatus.COMPANY_FORBIDDEN);
    }
}
