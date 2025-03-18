package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.req.CreateCompanyReq;
import com.pitchain.dto.req.LoginCompanyReq;
import com.pitchain.dto.req.VerifyCompanyReq;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/companies")
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "회사 회원가입")
    @PostMapping()
    public CustomApiResponse<Void> createCompany(@RequestBody CreateCompanyReq req) {
        companyService.createCompany(req);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회사 로그인")
    @PostMapping("/login")
    public CustomApiResponse<LoginRes> loginCompany(@RequestBody LoginCompanyReq req) {
        LoginRes loginRes = companyService.loginCompany(req);
        return CustomApiResponse.onSuccess(loginRes);
    }

    @Operation(summary = "회사 비밀번호 수정")
    @PutMapping("/passwords")
    public CustomApiResponse<Void> updatePassword(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody UpdatePasswordReq req) {
        companyService.updatePassword(memberDetails, req);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회사 인증")
    @PostMapping("/verify")
    public CustomApiResponse<Void> verifyCompany(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody VerifyCompanyReq req) {
        companyService.verifyCompany(memberDetails, req);
        return CustomApiResponse.onSuccess();
    }
}
