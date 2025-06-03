package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.controller.UpdatePasswordReq;
import com.pitchain.dto.req.CreateCompanyReq;
import com.pitchain.dto.req.LoginCompanyReq;
import com.pitchain.dto.req.VerifyCompanyReq;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.repository.CompanyRepository;
import com.pitchain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;

    private static final String COMPANY_EMAIL = "companyEmail";
    private static final String COMPANY_PASSWORD = "companyPassword";
    private static final String COMPANY_PASSWORD_CONFIRMATION = "companyPassword";

    private Company saveCompany() {
        Member member = memberRepository.save(Member.createCompanyMember(COMPANY_EMAIL));
        String encodedPassword = passwordEncoder.encode(COMPANY_PASSWORD);
        return companyRepository.save(new Company(member, encodedPassword));
    }

    private MemberDetails createCompanyMemberDetails(Company company) {
        return new MemberDetails(company.getMember().getId(), MemberRole.COMPANY);
    }

    @Test
    void 회사_가입_성공() {
        //given
        CreateCompanyReq req = new CreateCompanyReq(COMPANY_EMAIL, COMPANY_PASSWORD, COMPANY_PASSWORD_CONFIRMATION);

        //when
        companyService.createCompany(req);

        //then
        List<Company> all = companyRepository.findAll();
        Company company = all.get(0);
        assertThat(all).hasSize(1);
        assertThat(company.getMember().getEmail()).isEqualTo(COMPANY_EMAIL);
        assertThat(passwordEncoder.matches(COMPANY_PASSWORD, company.getPassword())).isTrue();
    }

    @Test
    void 회사_가입_실패() {
        //given
        saveCompany();
        CreateCompanyReq req = new CreateCompanyReq(COMPANY_EMAIL, COMPANY_PASSWORD, COMPANY_PASSWORD_CONFIRMATION);

        //when
        GeneralException e = assertThrows(GeneralException.class, () -> companyService.createCompany(req));

        //then
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.COMPANY_EMAIL_CONFLICT);
    }

    @Test
    void 회사_가입_실패_비밀번호_확인_실패인_경우() {
        //given
        String wrongPasswordConfirmation = "wrongPasswordConfirmation";
        CreateCompanyReq req = new CreateCompanyReq(COMPANY_EMAIL, COMPANY_PASSWORD, wrongPasswordConfirmation);

        //when
        GeneralException e = assertThrows(GeneralException.class, () -> companyService.createCompany(req));

        //then
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.COMPANY_PASSWORD_UNCONFIRMED);
    }


    @Test
    void 회사_로그인_성공() {
        //given
        Company company = saveCompany();
        LoginCompanyReq req = new LoginCompanyReq(COMPANY_EMAIL, COMPANY_PASSWORD);

        //when
        LoginRes loginRes = companyService.loginCompany(req);

        //then
        Long id_1 = tokenUtil.decodedJWT(loginRes.getAccessToken()).getClaim("id").asLong();
        Long id_2 = tokenUtil.decodedJWT(loginRes.getRefreshToken()).getClaim("id").asLong();

        assertThat(id_1).isEqualTo(company.getId());
        assertThat(id_2).isEqualTo(company.getId());
    }

    @Test
    void 회사_로그인_실패() {
        //given
        saveCompany();

        String invalidEmail = "invalidEmail";
        String invalidPassword = "invalidPassword";

        //when
        GeneralException e_1 = assertThrows(GeneralException.class, () -> companyService.loginCompany(new LoginCompanyReq(invalidEmail, COMPANY_PASSWORD)));
        GeneralException e_2 = assertThrows(GeneralException.class, () -> companyService.loginCompany(new LoginCompanyReq(COMPANY_EMAIL, invalidPassword)));

        //then
        assertThat(e_1.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
        assertThat(e_2.getErrorStatus()).isEqualTo(ErrorStatus.COMPANY_PASSWORD_NOT_MATCHED);
    }

    @Test
    void 회사_비밀번호_수정_성공() {
        //given
        Company company = saveCompany();
        MemberDetails companyMemberDetails = createCompanyMemberDetails(company);

        String updatedPassword = "updatedPassword";
        UpdatePasswordReq req = new UpdatePasswordReq(COMPANY_PASSWORD, updatedPassword);

        //when
        companyService.updatePassword(companyMemberDetails, req);

        //then
        assertThat(passwordEncoder.matches(updatedPassword, company.getPassword())).isTrue();
    }

    @Test
    void 회사_인증_성공() {
        //given
        Company company = saveCompany();

        assertThat(company.getIsVerified()).isFalse();

        MemberDetails companyMemberDetails = createCompanyMemberDetails(company);
        String companyName = "companyName";
        VerifyCompanyReq req = new VerifyCompanyReq(companyName);

        //when
        companyService.verifyCompany(companyMemberDetails, req);

        //then
        Company foundCompany = companyRepository.findByMemberId(company.getMember().getId()).orElseThrow();
        assertThat(foundCompany.getIsVerified()).isTrue();
        assertThat(foundCompany.getMember().getName()).isEqualTo(companyName);
    }

}
