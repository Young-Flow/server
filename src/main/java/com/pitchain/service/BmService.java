package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.BmWithScrapDto;
import com.pitchain.dto.req.CreateBmReq;
import com.pitchain.dto.req.UpdateBmReq;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.dto.res.PtImgRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.MyBmHistory;
import com.pitchain.entity.PtImg;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.MyBmHistoryRepository;
import com.pitchain.repository.BmScrapRepository;
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

    public void createBm(Long memberId, CreateBmReq createBmReq, MultipartFile logoImg, MultipartFile descImg) {
        Member member = entityFacade.getMember(memberId);
        String logoImgKey = s3Service.uploadFile(logoImg, S3UploadTarget.COMPANY_LOGO);
        String descImgKey = s3Service.uploadFile(descImg, S3UploadTarget.COMPANY_DESC);

        Bm newBm = createBmReq.createBm(member, logoImgKey, descImgKey);
        newBm.addSubCategories(createBmReq.subCategories());

        bmRepository.save(newBm);
    }

    public BmDetailRes getBmDetail(Long memberId, Long bmId) {
        Member member = entityFacade.getMember(memberId);

        BmWithScrapDto bmWithScrapDto = bmRepository.getBmWithScrapDto(member.getId(), bmId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.BM_NOT_FOUND));
        Bm bm = bmWithScrapDto.getBm();

        long scrapCnt = bmScrapRepository.countByBm(bm);
        List<PtImgRes> ptImgResList = getPtImgResList(bmId);
        List<String> subCategories = bm.getKoreanSubCategories();

        String spURL = s3Service.getFileURL(bm.getSpKey());
        String logoImgURL = s3Service.getFileURL(bm.getLogoImgKey());
        String descImgURL = s3Service.getFileURL(bm.getDescImgKey());

        myBmHistoryRepository.findByMemberAndBm(member, bm)
                .orElseGet(() -> myBmHistoryRepository.save(new MyBmHistory(member, bm)));

        return BmDetailRes.createRes(bmWithScrapDto, scrapCnt, ptImgResList, subCategories, spURL, logoImgURL, descImgURL);
    }

    public void updateBm(Long memberId, Long bmId, UpdateBmReq updateBmReq, MultipartFile logoImg, MultipartFile descImg) {
        Member member = entityFacade.getMember(memberId);
        Bm bm = entityFacade.getBm(bmId);

        validateBmOwner(bm, member);

        String logoImgKey = s3Service.uploadFile(logoImg, S3UploadTarget.COMPANY_LOGO);
        String descImgKey = s3Service.uploadFile(descImg, S3UploadTarget.COMPANY_DESC);

        bm.updateSubCategories(updateBmReq.subCategories());
        Bm updateBm = updateBmReq.createBm(logoImgKey, descImgKey);
        bm.update(updateBm);
    }

    public void updatePtImgs(Long memberId, Long bmId, List<MultipartFile> uploadPtImgs) {
        Member member = entityFacade.getMember(memberId);
        Bm bm = bmRepository.getByIdWithPtImgs(bmId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.BM_NOT_FOUND));

        validateBmOwner(bm, member);

        deletePtImgs(bm);
        List<PtImg> uploadedPtImgs = uploadPtImgs(uploadPtImgs, bm);

        bm.updatePtImgs(uploadedPtImgs);
    }

    public void deleteBm(Long memberId, Long bmId) {
        Member member = entityFacade.getMember(memberId);
        Bm bm = entityFacade.getBm(bmId);

        validateBmOwner(bm, member);

        bmRepository.delete(bm);
    }

    private List<PtImgRes> getPtImgResList(Long bmId) {
        List<PtImg> ptImgs = bmRepository.getPtImgsByBmId(bmId);
        List<PtImgRes> ptImgResList = ptImgs.stream()
                .map(ptImg -> PtImgRes.createRes(ptImg.getSerialNum(),
                        s3Service.getFileURL(ptImg.getImgKey())))
                .toList();
        return ptImgResList;
    }

    private void deletePtImgs(Bm bm) {
        List<PtImg> ptImgs = bm.getPtImgs();
        ptImgs.forEach(pi -> s3Service.deleteImg(pi.getImgKey()));
    }

    private List<PtImg> uploadPtImgs(List<MultipartFile> ptImgs, Bm bm) {
        List<PtImg> uploadPtImgs = new ArrayList<>();
        for (int serialNum = 0; ptImgs != null && serialNum < ptImgs.size(); serialNum++) {
            String uploadFileKey = s3Service.uploadFile(ptImgs.get(serialNum), S3UploadTarget.COMPANY_PT);
            PtImg ptImg = new PtImg(bm, serialNum, uploadFileKey);
            uploadPtImgs.add(ptImg);
        }
        return uploadPtImgs;
    }

    private static void validateBmOwner(Bm bm, Member member) {
        if (!bm.isOwner(member.getId()))
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);
    }
}
