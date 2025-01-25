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

import static com.pitchain.service.S3Service.getFileURL;

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

        String spOriginKey = s3Service.uploadFile(spVid, S3UploadTarget.COMPANY_VIDEO);

        //TO DO - AWS Lambda에서 정상적으로 트랜스코딩 완료됐으면 여기로 알려주기

        String spKey = createSpM3U8Key(spOriginKey);
        String thumbnailImgKey = s3Service.uploadFile(thumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL);

        Sp sp = new Sp(bm, spKey, thumbnailImgKey, createSpReq.name());
        spRepository.save(sp);
    }

    @Transactional(readOnly = true)
    public List<SpDetailRes> getSpDetails(Long memberId) {
        List<SpWithLikeDto> spWithLikeDtos = spRepository.findAllWithLike(memberId);

        return spWithLikeDtos.stream()
                .map(spWithLikeDto -> {
                    Sp sp = spWithLikeDto.getSp();
                    String spURL = getFileURL(sp.getSpKey());
                    String thumbnailImgURL = getFileURL(sp.getThumbnailImgKey());

                    Bm bm = sp.getBm();
                    long likeCnt = myBmRepository.countByBm(bm);
                    List<String> subCategories = bm.getSubCategories();

                    String logoImgURL = getFileURL(bm.getLogoImgKey());
                    return SpDetailRes.createRes(spWithLikeDto, spURL,thumbnailImgURL, likeCnt, subCategories, logoImgURL);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public SpDetailRes getSpDetail(Long memberId, Long spId) {
        SpWithLikeDto spWithLikeDto = spRepository.findSpWithLike(memberId, spId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.SP_NOT_FOUND));
        Sp sp = spWithLikeDto.getSp();
        String spURL = getFileURL(sp.getSpKey());
        String thumbnailImgURL = getFileURL(sp.getThumbnailImgKey());
        Bm bm = sp.getBm();
        long likeCnt = myBmRepository.countByBm(bm);
        List<String> subCategories = bm.getSubCategories();
        String logoImgURL = getFileURL(bm.getLogoImgKey());


        return SpDetailRes.createRes(spWithLikeDto, spURL, thumbnailImgURL, likeCnt, subCategories, logoImgURL);
    }

    public void updateSp(Long memberId, Long spId, String name, MultipartFile spVid, MultipartFile thumbnailImg) {
        Member member = entityFacade.getMember(memberId);
        Sp sp = entityFacade.getSp(spId);

        validateSpOwner(sp, member);

        s3Service.deleteFile(sp.getSpKey());
        s3Service.deleteFile(sp.getThumbnailImgKey());

        String spKey = s3Service.uploadFile(spVid, S3UploadTarget.COMPANY_VIDEO);
        String thumbnailImgKey = s3Service.uploadFile(thumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL);

        Sp updateSp = new Sp(sp.getBm(), spKey, thumbnailImgKey, name);
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

    private String createSpM3U8Key(String spKey) {
        String removedFileKey = removeFileExtension(spKey);
        return removedFileKey + ".m3u8";
    }

    public String removeFileExtension(String originalFileName) {
        return originalFileName.split("\\.")[0];
    }
}
