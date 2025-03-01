package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.req.CreateCompanyReq;
import com.pitchain.dto.req.UpdateCompanyReq;
import com.pitchain.dto.res.CompanyDetailRes;
import com.pitchain.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/companies")
@RestController
public class CompanyController {

    private final CompanyService companyService;


    @Operation(summary = "회사 생성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse<Void> createCompany(@AuthenticationPrincipal Long memberId,
                                                 @RequestPart CreateCompanyReq req,
                                                 @RequestPart(required = false) MultipartFile logoImg) {
        companyService.createCompany(memberId, req, logoImg);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회사 상세 조회")
    @GetMapping("{companyId}")
    public CustomApiResponse<CompanyDetailRes> getCompanyDetail(@AuthenticationPrincipal Long memberId,
                                                                @PathVariable("companyId") Long companyId) {
        CompanyDetailRes companyDetailRes = companyService.getCompanyDetail(memberId, companyId);
        return CustomApiResponse.onSuccess(companyDetailRes);
    }

    @Operation(summary = "나의 회사 목록 조회")
    @GetMapping
    public CustomApiResponse<List<CompanyDetailRes>> getMyCompanyDetails(@AuthenticationPrincipal Long memberId) {
        List<CompanyDetailRes> companyDetailsResList = companyService.getMyCompanyDetails(memberId);
        return CustomApiResponse.onSuccess(companyDetailsResList);
    }

    @Operation(summary = "회사 정보 수정")
    @PutMapping("{companyId}/info")
    public CustomApiResponse<Void> updateCompanyInfo(@AuthenticationPrincipal Long memberId,
                                                     @PathVariable("companyId") Long companyId,
                                                     @RequestBody UpdateCompanyReq req) {
        companyService.updateCompanyInfo(memberId, companyId, req);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회사 로고 이미지 수정")
    @PutMapping("{companyId}/logo-img")
    public CustomApiResponse<Void> updateCompanyLogoImg(@AuthenticationPrincipal Long memberId,
                                                        @PathVariable("companyId") Long companyId,
                                                        @RequestPart MultipartFile logoImg) {
        companyService.updateCompanyLogoImg(memberId, companyId, logoImg);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회사 삭제")
    @DeleteMapping("{companyId}")
    public CustomApiResponse<Void> deleteCompany(@AuthenticationPrincipal Long memberId,
                                                 @PathVariable("companyId") Long companyId) {
        companyService.deleteCompany(memberId, companyId);
        return CustomApiResponse.onSuccess();
    }
}
