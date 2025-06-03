package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.entity.InfinityScrollRes;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.common.util.InfinityScrollUtil;
import com.pitchain.dto.SpWithLikeDto;
import com.pitchain.dto.req.CreateSpReq;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.entity.*;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class SpService {
    private final EntityFacade entityFacade;
    private final SpRepository spRepository;
    private final SpRepositoryCustom spRepositoryCustom;
    private final SpLikeRepository spLikeRepository;
    private final S3Service s3Service;

    public void createSp(MemberDetails memberDetails, CreateSpReq createSpReq, MultipartFile spVid, MultipartFile thumbnailImg) {
        Company company = entityFacade.getCompany(memberDetails);
        Bm bm = entityFacade.getBm(createSpReq.bmId());

        String spOriginKey = s3Service.uploadFile(spVid, S3UploadTarget.COMPANY_VIDEO);

        String spKey = createSpM3U8Key(spOriginKey);
        String thumbnailImgKey = s3Service.uploadFile(thumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL);

        Sp sp = new Sp(bm, spKey, thumbnailImgKey, createSpReq.name());
        spRepository.save(sp);
    }

    @Transactional(readOnly = true)
    public List<SpDetailRes> getSpDetails(MemberDetails memberDetails) {
        List<SpWithLikeDto> spWithLikeDtos = spRepository.findAllWithLike(memberDetails.id());

        return spWithLikeDtos.stream()
                .map(spWithLikeDto -> {
                    Sp sp = spWithLikeDto.getSp();
                    Bm bm = sp.getBm();

                    Long likeCnt = spLikeRepository.countBySp(sp);
                    List<String> subCategories = bm.getKoreanSubCategories();

                    return SpDetailRes.createRes(spWithLikeDto, likeCnt, subCategories);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public InfinityScrollRes<SpDetailRes> getSpDetailsFilteredCategory(MemberDetails memberDetails, String mainCategoryInKorean, Long lastSpId, int size) {
        MainCategory mainCategory = MainCategory.from(mainCategoryInKorean);

        List<SpWithLikeDto> spWithLikeDtos = spRepositoryCustom.getSpWithLikeDtoFilteredCategory(memberDetails.id(), mainCategory, lastSpId, size);

        boolean hasNext = InfinityScrollUtil.hasNext(spWithLikeDtos.size(), size);
        if (hasNext)
            spWithLikeDtos.remove(spWithLikeDtos.size() - 1);
        Optional<SpWithLikeDto> lastElement = InfinityScrollUtil.getLastElement(spWithLikeDtos);
        Long lastElementId = lastElement.map(spWithLikeDto -> spWithLikeDto.getSp().getId()).orElse(null);

        List<SpDetailRes> content = spWithLikeDtos.stream()
                .map(spWithLikeDto -> {
                    Sp sp = spWithLikeDto.getSp();
                    Bm bm = sp.getBm();

                    Long likeCnt = spLikeRepository.countBySp(sp);
                    List<String> subCategories = bm.getKoreanSubCategories();

                    return SpDetailRes.createRes(spWithLikeDto, likeCnt, subCategories);
                })
                .toList();

        return InfinityScrollRes.createRes(content, lastElementId, hasNext);
    }

    @Transactional(readOnly = true)
    public SpDetailRes getSpDetail(MemberDetails memberDetails, Long spId) {
        SpWithLikeDto spWithLikeDto = spRepository.findSpWithLike(memberDetails.id(), spId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SP_NOT_FOUND));
        Sp sp = spWithLikeDto.getSp();
        Bm bm = sp.getBm();

        Long likeCnt = spLikeRepository.countBySp(sp);
        List<String> subCategories = bm.getKoreanSubCategories();

        return SpDetailRes.createRes(spWithLikeDto, likeCnt, subCategories);
    }

    public void updateSp(MemberDetails memberDetails, Long spId, String name, MultipartFile spVid, MultipartFile thumbnailImg) {
        Company company = entityFacade.getCompany(memberDetails);
        Sp sp = entityFacade.getSp(spId);

        validateSpOwner(sp, company);

        s3Service.deleteVid(sp.getSpKey());
        s3Service.deleteImg(sp.getThumbnailImgKey());

        String spKey = s3Service.uploadFile(spVid, S3UploadTarget.COMPANY_VIDEO);
        String thumbnailImgKey = s3Service.uploadFile(thumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL);

        Sp updateSp = new Sp(sp.getBm(), spKey, thumbnailImgKey, name);
        sp.update(updateSp);
    }

    public void deleteSp(MemberDetails memberDetails, Long spId) {
        Company company = entityFacade.getCompany(memberDetails);
        Sp sp = entityFacade.getSp(spId);

        validateSpOwner(sp, company);

        spRepository.delete(sp);
    }

    private static void validateSpOwner(Sp sp, Company company) {
        if (!sp.isOwner(company.getId())) {
            throw new GeneralException(ErrorStatus.COMPANY_FORBIDDEN);
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
