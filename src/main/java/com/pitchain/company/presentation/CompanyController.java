package com.pitchain.company.presentation;

import com.pitchain.common.annotation.RequiredRole;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.company.presentation.req.UpdatePasswordReq;
import com.pitchain.company.presentation.req.CompanyCreateReq;
import com.pitchain.company.presentation.req.CompanyLoginReq;
import com.pitchain.company.presentation.req.CompanyVerifyReq;
import com.pitchain.oauth.application.res.LoginRes;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.company.application.CompanyService;
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
