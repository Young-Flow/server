package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.entity.InfinityScrollRes;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.common.util.InfinityScrollUtil;
import com.pitchain.dto.SpWithLikeDto;
import com.pitchain.dto.req.SpCreateReq;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.BmSubCategory;
import com.pitchain.entity.Company;
import com.pitchain.entity.Sp;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.SpRepository;
import com.pitchain.repository.SpRepositoryCustom;
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
    private final BmSubcategoryService bmSubcategoryService;
    private final SpLikeService spLikeService;
    private final S3Service s3Service;

    public void createSp(MemberDetails memberDetails, SpCreateReq spCreateReq, MultipartFile thumbnailImg) {
        Company company = entityFacade.getCompany(memberDetails);
        Bm bm = entityFacade.getBm(spCreateReq.bmId());

        String thumbnailImgKey = s3Service.uploadFile(thumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL);

        Sp sp = Sp.of(bm, thumbnailImgKey, spCreateReq.name());
        spRepository.save(sp);
    }

    @Transactional(readOnly = true)
    public List<SpDetailRes> getSpDetails(MemberDetails memberDetails) {
        List<SpWithLikeDto> spWithLikeDtos = spRepository.findAllWithLike(memberDetails.id());

        return spWithLikeDtos.stream()
                .map(spWithLikeDto -> {
                    Sp sp = spWithLikeDto.getSp();
                    Bm bm = sp.getBm();

                    Long likeCnt = spLikeService.countBySpId(sp.getId());
                    List<BmSubCategory> bmSubCategories = bmSubcategoryService.getBmSubCategoryByBmId(bm.getId());
                    List<String> subCategories = convertToKoreanName(bmSubCategories);

                    return SpDetailRes.createRes(spWithLikeDto, likeCnt, subCategories);
                })
                .toList();
    }

    private static List<String> convertToKoreanName(List<BmSubCategory> bmSubCategories) {
        List<String> subcategoriesInKorean = bmSubCategories.stream()
                .map(bmSubCategory -> bmSubCategory.getSubCategory().getKoreanName())
                .toList();
        return subcategoriesInKorean;
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

                    Long likeCnt = spLikeService.countBySpId(sp.getId());
                    List<BmSubCategory> bmSubCategories = bmSubcategoryService.getBmSubCategoryByBmId(bm.getId());
                    List<String> subCategories = convertToKoreanName(bmSubCategories);

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

        Long likeCnt = spLikeService.countBySpId(sp.getId());
        List<BmSubCategory> bmSubCategories = bmSubcategoryService.getBmSubCategoryByBmId(bm.getId());
        List<String> subCategories = convertToKoreanName(bmSubCategories);

        return SpDetailRes.createRes(spWithLikeDto, likeCnt, subCategories);
    }

    @Transactional
    public void updateSp(MemberDetails memberDetails, Long spId, String name, MultipartFile thumbnailImg) {
        Company company = entityFacade.getCompany(memberDetails);
        Sp sp = entityFacade.getSp(spId);

        validateSpOwner(sp, company);

        if (thumbnailImg != null) {
            s3Service.deleteImg(sp.getThumbnailImgKey());
            String thumbnailImgKey = s3Service.uploadFile(thumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL);
            sp.updateThumbnailImgKey(thumbnailImgKey);
        }

        sp.update(name);
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

    @Transactional(readOnly = true)
    public List<Sp> getSpsByBmId(Long bmId) {
        Bm bm = entityFacade.getBm(bmId);
        return spRepository.findAllByBmId(bm.getId());
    }
}
