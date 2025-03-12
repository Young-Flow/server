package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.controller.UpdatePasswordReq;
import com.pitchain.dto.req.CreateCompanyReq;
import com.pitchain.dto.req.LoginCompanyReq;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.repository.CompanyRepository;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CompanyService {
    private final EntityFacade entityFacade;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;

    public void createCompany(CreateCompanyReq req) {
        Optional<Member> optionalCompany = memberRepository.findByEmail(req.email());
        verifyEmailConflict(optionalCompany);
        confirmPassword(req.password(), req.passwordConfirmation());

        Member member = Member.fromCompany(req.email());
        memberRepository.save(member);

        String encodedPassword = passwordEncoder.encode(req.password());
        Company company = req.createUnverifiedCompany(member, encodedPassword);
        companyRepository.save(company);
    }

    public LoginRes loginCompany(LoginCompanyReq req) {
        Member member = memberRepository.findByEmail(req.email())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Company company = companyRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.COMPANY_NOT_FOUND));

        verifyPassword(req.password(), company.getPassword());

        String accessToken = tokenUtil.issueAccessToken(company.getId(), MemberRole.COMPANY);
        String refreshToken = tokenUtil.issueRefreshToken(company.getId(), MemberRole.COMPANY);

        return LoginRes.createRes(accessToken, refreshToken);
    }

    public void updatePassword(MemberDetails memberDetails, UpdatePasswordReq req) {
        Company company = entityFacade.getCompany(memberDetails);

        verifyPassword(req.originPassword(), company.getPassword());

        String encodedNewPassword = passwordEncoder.encode(req.newPassword());
        company.updatePassword(encodedNewPassword);
    }

    private static void verifyEmailConflict(Optional<Member> optionalMember) {
        if (optionalMember.isPresent())
            throw new GeneralHandler(ErrorStatus.COMPANY_EMAIL_CONFLICT);
    }

    private void confirmPassword(String password, String passwordConfirmation) {
        if (!password.equals(passwordConfirmation))
            throw new GeneralHandler(ErrorStatus.COMPANY_PASSWORD_UNCONFIRMED);
    }

    private void verifyPassword(String inputPassword, String encodedPassword) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword))
            throw new GeneralHandler(ErrorStatus.COMPANY_PASSWORD_NOT_MATCHED);
    }
}
