package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.SpWithLikeDto;
import com.pitchain.dto.req.CreateSpReq;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.Sp;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.MyBmRepository;
import com.pitchain.repository.SpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class SpService {
    private final EntityFacade entityFacade;
    private final SpRepository spRepository;
    private final MyBmRepository myBmRepository;
    private final S3Service s3Service;

    public void createSp(Long memberId, CreateSpReq createSpReq, MultipartFile spVid, MultipartFile thumbnailImg) {
        Member member = entityFacade.getMember(memberId);
        Bm bm = entityFacade.getBm(createSpReq.bmId());

        String spVidURL = s3Service.uploadFile(spVid, S3UploadTarget.COMPANY_VIDEO);
        String thumbnailImgURL = s3Service.uploadFile(thumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL);

        Sp sp = new Sp(bm, spVidURL, thumbnailImgURL, createSpReq.name());
        spRepository.save(sp);
    }

    @Transactional(readOnly = true)
    public List<SpDetailRes> getSpDetails(Long memberId) {
        List<SpWithLikeDto> spWithLikeDtos = spRepository.findAllWithLike(memberId);
        return spWithLikeDtos.stream()
                .map(spWithLikeDto -> {
                    Sp sp = spWithLikeDto.getSp();
                    long likeCnt = myBmRepository.countByBm(sp.getBm());
                    return SpDetailRes.createRes(spWithLikeDto, likeCnt);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public SpDetailRes getSpDetail(Long memberId, Long spId) {
        SpWithLikeDto spWithLikeDto = spRepository.findSpWithLike(memberId, spId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.SP_NOT_FOUND));

        long likeCnt = myBmRepository.countByBm(spWithLikeDto.getSp().getBm());

        return SpDetailRes.createRes(spWithLikeDto, likeCnt);
    }

    public void updateSp(Long memberId, Long spId, String name, MultipartFile spVid, MultipartFile thumbnailImg) {
        Member member = entityFacade.getMember(memberId);
        Sp sp = entityFacade.getSp(spId);

        validateSpOwner(sp, member);

        s3Service.deleteFile(sp.getShortPitchURL());
        s3Service.deleteFile(sp.getThumbnailImg());

        String spVidURL = s3Service.uploadFile(spVid, S3UploadTarget.COMPANY_VIDEO);
        String thumbnailImgURL = s3Service.uploadFile(thumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL);

        Sp updateSp = new Sp(sp.getBm(), spVidURL, thumbnailImgURL, name);
        sp.update(updateSp);
    }

    public void deleteSp(Long memberId, Long spId) {
        Member member = entityFacade.getMember(memberId);
        Sp sp = entityFacade.getSp(spId);

        validateSpOwner(sp, member);

        spRepository.delete(sp);
    }

    private static void validateSpOwner(Sp sp, Member member) {
        if (!sp.isOwner(member.getId())) {
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);
        }
    }
}
