package com.pitchain.controller;

import com.pitchain.common.annotation.RequiredRole;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.dto.req.CompanyCreateReq;
import com.pitchain.dto.req.CompanyLoginReq;
import com.pitchain.dto.req.CompanyVerifyReq;
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
    public void createCompany(@RequestBody CompanyCreateReq req) {
        companyService.createCompany(req);
    }

    @Operation(summary = "회사 로그인")
    @PostMapping("/login")
    public LoginRes loginCompany(@RequestBody CompanyLoginReq req) {
        LoginRes loginRes = companyService.loginCompany(req);
        return loginRes;
    }

    @RequiredRole(MemberRole.COMPANY)
    @Operation(summary = "회사 비밀번호 수정")
    @PutMapping("/passwords")
    public void updatePassword(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody UpdatePasswordReq req) {
        companyService.updatePassword(memberDetails, req);
    }

    @RequiredRole(MemberRole.COMPANY)
    @Operation(summary = "회사 인증")
    @PostMapping("/verify")
    public void verifyCompany(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody CompanyVerifyReq req) {
        companyService.verifyCompany(memberDetails, req);
    }
}
