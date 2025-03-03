package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.req.CreateSpReq;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.service.SpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/sps")
@RestController
public class SpController {
    private final SpService spService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse createSp(@AuthenticationPrincipal Long companyId,
                                      @RequestPart CreateSpReq createSpReq,
                                      @RequestPart(required = false) MultipartFile spVideo,
                                      @RequestPart(required = false) MultipartFile thumbnailImg) {
        spService.createSp(companyId, createSpReq, spVideo, thumbnailImg);
        return CustomApiResponse.onSuccess();
    }

    @GetMapping
    public CustomApiResponse<List<SpDetailRes>> getSpDetails(@AuthenticationPrincipal Long memberId) {
        List<SpDetailRes> spDetailResList = spService.getSpDetails(memberId);
        return CustomApiResponse.onSuccess(spDetailResList);
    }

    @GetMapping("/category")
    public CustomApiResponse<List<SpDetailRes>> getSpDetailsFilteredCategory(@AuthenticationPrincipal Long memberId,
                                                                             @RequestParam String mainCategoryInKorean) {
        List<SpDetailRes> spDetailResList = spService.getSpDetailsFilteredCategory(memberId, mainCategoryInKorean);
        return CustomApiResponse.onSuccess(spDetailResList);
    }

    @GetMapping("/{spId}")
    public CustomApiResponse<SpDetailRes> getSpDetail(@AuthenticationPrincipal Long memberId,
                                                      @PathVariable Long spId) {
        SpDetailRes spDetailRes = spService.getSpDetail(memberId, spId);
        return CustomApiResponse.onSuccess(spDetailRes);
    }

    @GetMapping("/recommendation")
    public CustomApiResponse<List<SpDetailRes>> getSpDetailsRecommendedFromAi(@AuthenticationPrincipal Long memberId,
                                                                              @RequestParam List<Long> bmIds) {
        List<SpDetailRes> spDetailResList = spService.getSpDetailsRecommendedFromAi(memberId, bmIds);
        return CustomApiResponse.onSuccess(spDetailResList);
    }

    @PutMapping(value = "/{spId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse updateSp(@AuthenticationPrincipal Long companyId,
                                      @PathVariable Long spId,
                                      @RequestPart String name,
                                      @RequestPart(required = false) MultipartFile spVideo,
                                      @RequestPart(required = false) MultipartFile thumbnailImg) {
        spService.updateSp(companyId, spId, name, spVideo, thumbnailImg);
        return CustomApiResponse.onSuccess();
    }

    @DeleteMapping("/{spId}")
    public CustomApiResponse deleteSp(@AuthenticationPrincipal Long companyId,
                                      @PathVariable Long spId) {
        spService.deleteSp(companyId, spId);
        return CustomApiResponse.onSuccess();
    }
}
