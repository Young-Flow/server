package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.req.CreateCompanyReq;
import com.pitchain.dto.req.LoginCompanyReq;
import com.pitchain.dto.req.UpdateCompanyReq;
import com.pitchain.dto.res.CompanyDetailRes;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/companies")
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "회사 생성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse<Void> createCompany(@RequestPart CreateCompanyReq req) {
        companyService.createCompany(req);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회사 로그인")
    @PostMapping("/login")
    public CustomApiResponse<LoginRes> loginCompany(@RequestBody LoginCompanyReq req) {
        LoginRes loginRes = companyService.loginCompany(req);
        return CustomApiResponse.onSuccess(loginRes);
    }

    @Operation(summary = "회사 이메일 중복 여부 확인", description = "회사 생성 및 수정 시 사용, 중복이면 true 반환")
    @GetMapping("/emails")
    public CustomApiResponse<Boolean> checkEmail(@RequestParam String email) {
        boolean isDuplicated = companyService.isDuplicatedEmail(email);
        return CustomApiResponse.onSuccess(isDuplicated);
    }

    @Operation(summary = "회사 조회", description = "회사 정보 수정 시 사용")
    @GetMapping
    public CustomApiResponse<CompanyDetailRes> getCompanyDetail(@AuthenticationPrincipal Long companyId) {
        CompanyDetailRes companyDetailRes = companyService.getCompanyDetail(companyId);
        return CustomApiResponse.onSuccess(companyDetailRes);
    }

    @Operation(summary = "회사 이메일 수정")
    @PutMapping("/emails")
    public CustomApiResponse<Void> updateEmail(@AuthenticationPrincipal Long companyId, @RequestBody String email) {
        companyService.updateEmail(companyId, email);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회사 비밀번호 수정")
    @PutMapping("/passwords")
    public CustomApiResponse<Void> updatePassword(@AuthenticationPrincipal Long companyId, @RequestBody UpdatePasswordReq req) {
        companyService.updatePassword(companyId, req);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회사 로고 이미지 수정")
    @PutMapping("/logoImgs")
    public CustomApiResponse<Void> updateLogoImg(@AuthenticationPrincipal Long companyId, @RequestPart MultipartFile logoImg) {
        companyService.updateLogoImg(companyId, logoImg);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회사 정보 수정")
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse<Void> updateCompany(@AuthenticationPrincipal Long companyId,
                                                 @RequestPart UpdateCompanyReq req) {
        companyService.updateCompanyInfo(companyId, req);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회사 삭제")
    @DeleteMapping("{companyId}")
    public CustomApiResponse<Void> deleteCompany(@AuthenticationPrincipal Long companyId) {
        companyService.deleteCompany(companyId);
        return CustomApiResponse.onSuccess();
    }
}
