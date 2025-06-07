package com.pitchain.controller;

import com.pitchain.dto.req.BmCreateReq;
import com.pitchain.dto.req.BmUpdateReq;
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
    public void createBm(@AuthenticationPrincipal MemberDetails memberDetails,
                         @Valid @RequestPart BmCreateReq bmCreateReq,
                         @RequestPart(required = false) MultipartFile descImg) {
        bmService.createBm(memberDetails, bmCreateReq, descImg);
    }

    @Operation(summary = "BM 상세 조회")
    @GetMapping("{bmId}")
    public BmDetailRes getBmDetail(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable("bmId") Long bmId) {
        BmDetailRes bmDetailRes = bmService.getBmDetail(memberDetails, bmId);
        return bmDetailRes;
    }

    @Operation(summary = "BM 수정")
    @PutMapping(value = "{bmId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateBm(@AuthenticationPrincipal MemberDetails memberDetails,
                         @PathVariable("bmId") Long bmId,
                         @Valid @RequestPart BmUpdateReq bmUpdateReq,
                         @RequestPart(required = false) MultipartFile descImg) {
        bmService.updateBm(memberDetails, bmId, bmUpdateReq, descImg);
    }

    @Operation(summary = "BM PT 이미지 수정")
    @PostMapping(value = "{bmId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updatePtImgs(@AuthenticationPrincipal MemberDetails memberDetails,
                             @PathVariable Long bmId,
                             @RequestPart(required = false) List<String> ptImgKeys) {
        bmService.updatePtImgs(memberDetails, bmId, ptImgKeys);
    }

    @Operation(summary = "BM 삭제")
    @DeleteMapping("{bmId}")
    public void deleteBm(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable("bmId") Long bmId) {
        bmService.deleteBm(memberDetails, bmId);
    }
}
