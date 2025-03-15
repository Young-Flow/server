package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.req.CreateBmReq;
import com.pitchain.dto.req.UpdateBmReq;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.service.BmService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
    public CustomApiResponse createBm(@AuthenticationPrincipal MemberDetails memberDetails,
                                      @Valid @RequestPart CreateBmReq createBmReq,
                                      @RequestPart(required = false) MultipartFile descImg) { // todo 텍스트 에디터 도입 시 수정 필요
        bmService.createBm(memberDetails, createBmReq, descImg);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "BM 상세 조회")
    @GetMapping("{bmId}")
    public CustomApiResponse<BmDetailRes> getBmDetail(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable("bmId") Long bmId) {
        BmDetailRes bmDetailRes = bmService.getBmDetail(memberDetails, bmId);
        return CustomApiResponse.onSuccess(bmDetailRes);
    }

    @Operation(summary = "BM 수정")
    @PutMapping(value = "{bmId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse<BmDetailRes> updateBm(@AuthenticationPrincipal MemberDetails memberDetails,
                                                   @PathVariable("bmId") Long bmId,
                                                   @Valid @RequestPart UpdateBmReq updateBmReq,
                                                   @RequestPart(required = false) MultipartFile descImg) {
        bmService.updateBm(memberDetails, bmId, updateBmReq, descImg);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "BM PT 이미지 수정")
    @PostMapping(value = "{bmId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse updatePtImgs(@AuthenticationPrincipal MemberDetails memberDetails,
                                          @PathVariable Long bmId,
                                          @RequestPart(required = false) List<String> ptImgKeys) {
        bmService.updatePtImgs(memberDetails, bmId, ptImgKeys);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "BM 삭제")
    @DeleteMapping("{bmId}")
    public CustomApiResponse deleteBm(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable("bmId") Long bmId) {
        bmService.deleteBm(memberDetails, bmId);
        return CustomApiResponse.onSuccess();
    }
}
