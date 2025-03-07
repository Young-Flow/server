package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.controller.UpdatePasswordReq;
import com.pitchain.dto.req.CreateCompanyReq;
import com.pitchain.dto.req.LoginCompanyReq;
import com.pitchain.dto.res.CompanyDetailRes;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.entity.Company;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class CompanyServiceTest {

    @MockBean
    private S3Service s3Service;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String COMPANY_EMAIL = "companyEmail";
    private static final String COMPANY_PASSWORD = "companyPassword";
    private static final String COMPANY_PASSWORD_CONFIRMATION = "companyPassword";
    private static final String COMPANY_NAME = "companyName";
    private static final String COMPANY_ADDRESS = "companyAddress";

    private static final String LOGO_IMG_KEY = "bm_logo_img_key";
    private static final MockMultipartFile LOGO_IMG = new MockMultipartFile(
            "logo", "logo.png", "image/png", "test data 1".getBytes());

    private Company saveCompany() {
        return companyRepository.save(new Company(COMPANY_EMAIL, passwordEncoder.encode(COMPANY_PASSWORD), false, COMPANY_NAME, COMPANY_ADDRESS, LOGO_IMG_KEY));
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
        assertThat(company.getEmail()).isEqualTo(COMPANY_EMAIL);
        assertThat(passwordEncoder.matches(COMPANY_PASSWORD, company.getPassword())).isTrue();
    }

    @Test
    void 회사_가입_실패() {
        //given
        saveCompany();
        CreateCompanyReq req = new CreateCompanyReq(COMPANY_EMAIL, COMPANY_PASSWORD, COMPANY_PASSWORD_CONFIRMATION);

        //when
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> companyService.createCompany(req));

        //then
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.COMPANY_EMAIL_CONFLICT);
    }

    @Test
    void 회사_가입_실패_비밀번호_확인_실패인_경우() {
        //given
        String wrongPasswordConfirmation = "wrongPasswordConfirmation";
        CreateCompanyReq req = new CreateCompanyReq(COMPANY_EMAIL, COMPANY_PASSWORD, wrongPasswordConfirmation);

        //when
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> companyService.createCompany(req));

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
        GeneralHandler e_1 = assertThrows(GeneralHandler.class, () -> companyService.loginCompany(new LoginCompanyReq(invalidEmail, COMPANY_PASSWORD)));
        GeneralHandler e_2 = assertThrows(GeneralHandler.class, () -> companyService.loginCompany(new LoginCompanyReq(COMPANY_EMAIL, invalidPassword)));

        //then
        assertThat(e_1.getErrorStatus()).isEqualTo(ErrorStatus.COMPANY_NOT_FOUND);
        assertThat(e_2.getErrorStatus()).isEqualTo(ErrorStatus.COMPANY_PASSWORD_NOT_MATCHED);
    }

    @Test
    void 회사_이메일_중복_확인_중복인_경우() {
        //given
        Company company = saveCompany();

        //when
        boolean isDuplicated = companyService.isDuplicatedEmail(company.getEmail());

        //then
        assertThat(isDuplicated).isTrue();
    }

    @Test
    void 회사_이메일_중복_확인_중복이_아닌_경우() {
        //given
        saveCompany();
        String newEmail = "newEmail";

        //when
        boolean isDuplicated = companyService.isDuplicatedEmail(newEmail);

        //then
        assertThat(isDuplicated).isFalse();
    }

    @Test
    void 회사_상세조회_성공() {
        //given
        Company company = saveCompany();

        when(s3Service.uploadFile(LOGO_IMG, S3UploadTarget.COMPANY_LOGO))
                .thenReturn(LOGO_IMG_KEY);

        //when
        CompanyDetailRes companyDetailRes = companyService.getCompanyDetail(company.getId());

        //then
        assertThat(companyDetailRes.email()).isEqualTo(company.getEmail());
        assertThat(companyDetailRes.logoImgKey()).isEqualTo(company.getLogoImgKey());
        assertThat(companyDetailRes.name()).isEqualTo(company.getName());
        assertThat(companyDetailRes.address()).isEqualTo(company.getAddress());
        assertThat(companyDetailRes.isVerified()).isEqualTo(company.getIsVerified());
    }

    @Test
    void 회사_이메일_수정_성공() {
        //given
        Company company = saveCompany();

        String newEmail = "updatedName";

        //when
        companyService.updateEmail(company.getId(), newEmail);

        //then
        Company findCompany = companyRepository.findById(company.getId()).get();
        assertThat(findCompany.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void 회사_비밀번호_수정_성공() {
        //given
        Company company = saveCompany();

        String updatedPassword = "updatedPassword";
        UpdatePasswordReq req = new UpdatePasswordReq(COMPANY_PASSWORD, updatedPassword);

        //when
        companyService.updatePassword(company.getId(), req);

        //then
        Company findCompany = companyRepository.findById(company.getId()).get();
        assertThat(findCompany.getPassword()).isEqualTo(updatedPassword);
    }

    @Test
    void 회사_로고_이미지_수정_성공() {
        //given
        Company company = saveCompany();

        MockMultipartFile newLogoImg = new MockMultipartFile(
                "logo", "newLogo.png", "image/png", "test data 2".getBytes());
        String newLogoImgKey = "new_logo_img_key";
        when(s3Service.uploadFile(newLogoImg, S3UploadTarget.COMPANY_LOGO))
                .thenReturn(newLogoImgKey);

        //when
        companyService.updateLogoImg(company.getId(), newLogoImg);

        //then
        Company findCompany = companyRepository.findById(company.getId()).get();
        assertThat(findCompany.getLogoImgKey()).isEqualTo(newLogoImgKey);
    }

    @Test
    void 회사_삭제_성공() {
        //given
        Company company = saveCompany();

        //when
        companyService.deleteCompany(company.getId());

        //then
        List<Company> all = companyRepository.findAll();
        assertThat(all).isEmpty();
    }
}
