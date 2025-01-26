package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.req.CreateBmReq;
import com.pitchain.dto.req.UpdateBmReq;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.service.BmService;
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

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse createBm(@AuthenticationPrincipal Long memberId,
                                      @RequestPart CreateBmReq createBmReq,
                                      @RequestPart(required = false) MultipartFile logoImg,
                                      @RequestPart(required = false) MultipartFile descImg) {
        bmService.createBm(memberId, createBmReq, logoImg, descImg);
        return CustomApiResponse.onSuccess();
    }

    @GetMapping("{bmId}")
    public CustomApiResponse<BmDetailRes> getBmDetail(@AuthenticationPrincipal Long memberId,
                                                      @PathVariable("bmId") Long bmId) {
        BmDetailRes bmDetailRes = bmService.getBmDetail(memberId, bmId);
        return CustomApiResponse.onSuccess(bmDetailRes);
    }

    @PutMapping(value = "{bmId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse<BmDetailRes> updateBm(@AuthenticationPrincipal Long memberId,
                                                   @PathVariable("bmId") Long bmId,
                                                   @RequestPart UpdateBmReq updateBmReq,
                                                   @RequestPart(required = false) MultipartFile logoImg,
                                                   @RequestPart(required = false) MultipartFile descImg) {
        bmService.updateBm(memberId, bmId, updateBmReq, logoImg, descImg);
        return CustomApiResponse.onSuccess();
    }

    @PostMapping(value = "{bmId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse updatePtImgs(@AuthenticationPrincipal Long memberId,
                                          @PathVariable Long bmId,
                                          @RequestPart(required = false) List<MultipartFile> ptImgs) {
        bmService.updatePtImgs(memberId, bmId, ptImgs);
        return CustomApiResponse.onSuccess();
    }

    @DeleteMapping("{bmId}")
    public CustomApiResponse deleteBm(@AuthenticationPrincipal Long memberId,
                                      @PathVariable("bmId") Long bmId) {
        bmService.deleteBm(memberId, bmId);
        return CustomApiResponse.onSuccess();
    }
}
