package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.BmWithLikeDto;
import com.pitchain.dto.req.CreateBmReq;
import com.pitchain.dto.req.UpdateBmReq;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.dto.res.PtImgRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.PtImg;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.MyBmRepository;
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
    private final MyBmRepository myBmRepository;
    private final S3Uploader s3Uploader;

    public void createBm(Long memberId, CreateBmReq createBmReq, MultipartFile logoImg, MultipartFile descriptionImg) {
        Member member = entityFacade.getMember(memberId);
        String logoImgURL = s3Uploader.uploadFile(logoImg, S3UploadTarget.COMPANY_LOGO);
        String descriptionImgURL = s3Uploader.uploadFile(descriptionImg, S3UploadTarget.COMPANY_DESC);

        Bm newBm = createBmReq.createBm(member, logoImgURL, descriptionImgURL);
        bmRepository.save(newBm);
    }

    @Transactional(readOnly = true)
    public BmDetailRes getBmDetail(Long memberId, Long bmId) {
        Member member = entityFacade.getMember(memberId);

        BmWithLikeDto bmWithLikeDto = bmRepository.getBmWithLikeDto(member.getId(), bmId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.BM_NOT_FOUND));

        List<PtImg> ptImgs = bmRepository.getPtImgsByBmId(bmWithLikeDto.getBm().getId());
        List<PtImgRes> ptImgResList = ptImgs.stream()
                .map(PtImgRes::createRes)
                .toList();

        long likeCnt = myBmRepository.countByBm(bmWithLikeDto.getBm());

        return BmDetailRes.createRes(bmWithLikeDto, likeCnt, ptImgResList);
    }

    public void updateBm(Long memberId, Long bmId, UpdateBmReq updateBmReq, MultipartFile logoImg, MultipartFile descriptionImg) {
        Member member = entityFacade.getMember(memberId);
        Bm bm = entityFacade.getBm(bmId);

        validateBmOwner(bm, member);

        String logoImgURL = s3Uploader.uploadFile(logoImg, S3UploadTarget.COMPANY_LOGO);
        String descriptionImgURL = s3Uploader.uploadFile(descriptionImg, S3UploadTarget.COMPANY_DESC);

        Bm updateBm = updateBmReq.createBm(logoImgURL, descriptionImgURL);

        bm.update(updateBm);
    }

    public void updatePtImgs(Long memberId, Long bmId, List<MultipartFile> ptImgs) {
        Member member = entityFacade.getMember(memberId);
        Bm bm = entityFacade.getBm(bmId);

        validateBmOwner(bm, member);

        List<PtImg> uploadPtImgs = new ArrayList<>();
        for (int serialNum = 0; ptImgs != null && serialNum < ptImgs.size(); serialNum++) {
            String uploadFileURL = s3Uploader.uploadFile(ptImgs.get(serialNum), S3UploadTarget.COMPANY_PT);
            PtImg ptImg = new PtImg(bm, serialNum, uploadFileURL);
            uploadPtImgs.add(ptImg);
        }

        bm.updatePtImgs(uploadPtImgs);
    }

    public void deleteBm(Long memberId, Long bmId) {
        Member member = entityFacade.getMember(memberId);
        Bm bm = entityFacade.getBm(bmId);

        validateBmOwner(bm, member);

        bmRepository.delete(bm);
    }

    private static void validateBmOwner(Bm bm, Member member) {
        if (!bm.isOwner(member.getId()))
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);
    }
}
