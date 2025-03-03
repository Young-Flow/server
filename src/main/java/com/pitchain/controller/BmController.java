package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.req.CreateBmReq;
import com.pitchain.dto.req.UpdateBmReq;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.service.BmService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/bms")
@RestController
public class BmController {
    private final BmService bmService;

    @Operation(summary = "BM 생성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse createBm(@AuthenticationPrincipal Long companyId,
                                      @RequestPart CreateBmReq createBmReq,
                                      @RequestPart(required = false) MultipartFile descImg) { // todo 텍스트 에디터 도입 시 수정 필요
        bmService.createBm(companyId, createBmReq, descImg);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "BM 상세 조회")
    @GetMapping("{bmId}")
    public CustomApiResponse<BmDetailRes> getBmDetail(@AuthenticationPrincipal Long memberId,
                                                      @PathVariable("bmId") Long bmId) {
        BmDetailRes bmDetailRes = bmService.getBmDetail(memberId, bmId);
        return CustomApiResponse.onSuccess(bmDetailRes);
    }

    @Operation(summary = "BM 수정")
    @PutMapping(value = "{bmId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse<BmDetailRes> updateBm(@AuthenticationPrincipal Long companyId,
                                                   @PathVariable("bmId") Long bmId,
                                                   @RequestPart UpdateBmReq updateBmReq,
                                                   @RequestPart(required = false) MultipartFile descImg) {
        bmService.updateBm(companyId, bmId, updateBmReq, descImg);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "BM PT 이미지 수정")
    @PostMapping(value = "{bmId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse updatePtImgs(@AuthenticationPrincipal Long companyId,
                                          @PathVariable Long bmId,
                                          @RequestPart(required = false) List<MultipartFile> ptImgs) {
        bmService.updatePtImgs(companyId, bmId, ptImgs);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "BM 삭제")
    @DeleteMapping("{bmId}")
    public CustomApiResponse deleteBm(@AuthenticationPrincipal Long companyId,
                                      @PathVariable("bmId") Long bmId) {
        bmService.deleteBm(companyId, bmId);
        return CustomApiResponse.onSuccess();
    }
}
