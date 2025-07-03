package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.controller.UpdatePasswordReq;
import com.pitchain.dto.req.CompanyCreateReq;
import com.pitchain.dto.req.CompanyLoginReq;
import com.pitchain.dto.req.CompanyVerifyReq;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.repository.CompanyRepository;
import com.pitchain.repository.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final EntityFacade entityFacade;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;

    @Transactional
    public void createCompany(CompanyCreateReq req) {
        confirmPassword(req.password(), req.passwordConfirmation());

        memberService.validateEmailConflict(req.email());

        Member member = memberService.saveCompanyMember(req.email());

        String encodedPassword = passwordEncoder.encode(req.password());
        Company company = req.createUnverifiedCompany(member, encodedPassword);
        companyRepository.save(company);
    }

    @Transactional(readOnly = true)
    public LoginRes loginCompany(CompanyLoginReq req) {
        Member member = memberService.findByEmail(req.email());

        Company company = companyRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMPANY_NOT_FOUND));

        verifyPassword(req.password(), company.getPassword());

        String accessToken = tokenUtil.issueAccessToken(company.getId(), MemberRole.COMPANY);
        String refreshToken = tokenUtil.issueRefreshToken(company.getId(), MemberRole.COMPANY);

        return LoginRes.createRes(accessToken, refreshToken);
    }

    @Transactional
    public void updatePassword(MemberDetails memberDetails, UpdatePasswordReq req) {
        Company company = entityFacade.getCompany(memberDetails);

        verifyPassword(req.originPassword(), company.getPassword());

        String newEncodedPassword = passwordEncoder.encode(req.newPassword());
        company.updatePassword(newEncodedPassword);
    }

    @Transactional
    public void verifyCompany(MemberDetails memberDetails, CompanyVerifyReq req) {
        Company company = entityFacade.getCompany(memberDetails);

        company.getMember().updateName(req.companyName());
        company.verifyCompany();
    }

    private void confirmPassword(String password, String passwordConfirmation) {
        if (!password.equals(passwordConfirmation))
            throw new GeneralException(ErrorStatus.COMPANY_PASSWORD_UNCONFIRMED);
    }

    private void verifyPassword(String inputPassword, String encodedPassword) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword))
            throw new GeneralException(ErrorStatus.COMPANY_PASSWORD_NOT_MATCHED);
    }
}
