package com.pitchain.service;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.dto.req.CreateCompanyReq;
import com.pitchain.dto.req.UpdateCompanyReq;
import com.pitchain.dto.res.CompanyDetailRes;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.repository.CompanyRepository;
import com.pitchain.repository.MemberRepository;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
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
    private MemberRepository memberRepository;

    private static final String COMPANY_NAME = "copanyName";
    private static final String COMPANY_ADDRESS = "companyAddress";

    private static final String LOGO_IMG_KEY = "bm_logo_img_key";
    private static final MockMultipartFile LOGO_IMG = new MockMultipartFile(
            "logo", "logo.png", "image/png", "test data 1".getBytes());

    private Member saveMember() {
        return memberRepository.save(new Member(Country.ROK, Strings.EMPTY));
    }

    private Company saveCompany(Member member) {
        return companyRepository.save(new Company(COMPANY_NAME, COMPANY_ADDRESS, LOGO_IMG_KEY, member));
    }

    @Test
    void 회사_생성_성공() {
        //given
        Member member = saveMember();


        CreateCompanyReq req = new CreateCompanyReq(COMPANY_NAME, COMPANY_ADDRESS);
        when(s3Service.uploadFile(LOGO_IMG, S3UploadTarget.COMPANY_LOGO))
                .thenReturn(LOGO_IMG_KEY);

        //when
        companyService.createCompany(member.getId(), req, LOGO_IMG);

        //then
        List<Company> all = companyRepository.findAll();
        Company company = all.get(0);
        assertThat(all).hasSize(1);
        assertThat(company.getName()).isEqualTo(COMPANY_NAME);
        assertThat(company.getAddress()).isEqualTo(COMPANY_ADDRESS);
        assertThat(company.getLogoImgKey()).isEqualTo(LOGO_IMG_KEY);
        assertThat(company.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    void 회사_상세조회_성공() {
        //given
        Member member = saveMember();
        Company company = saveCompany(member);

        //when
        CompanyDetailRes companyDetailRes = companyService.getCompanyDetail(member.getId(), company.getId());

        //then
        assertThat(companyDetailRes.name()).isEqualTo(company.getName());
        assertThat(companyDetailRes.address()).isEqualTo(company.getAddress());
        assertThat(companyDetailRes.logoImgKey()).isEqualTo(company.getLogoImgKey());
    }

    @Test
    void 나의_회사_상세조회_성공() {
        //given
        Member member = saveMember();
        Company company_1 = saveCompany(member);
        Company company_2 = saveCompany(member);

        //when
        List<CompanyDetailRes> companyDetailResList = companyService.getMyCompanyDetails(member.getId());

        //then
        List<Company> all = companyRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(companyDetailResList.size()).isEqualTo(2);
        assertThat(companyDetailResList.get(0).name()).isEqualTo(company_1.getName());
        assertThat(companyDetailResList.get(0).address()).isEqualTo(company_1.getAddress());
        assertThat(companyDetailResList.get(0).logoImgKey()).isEqualTo(company_1.getLogoImgKey());
        assertThat(companyDetailResList.get(1).name()).isEqualTo(company_2.getName());
        assertThat(companyDetailResList.get(1).address()).isEqualTo(company_2.getAddress());
        assertThat(companyDetailResList.get(1).logoImgKey()).isEqualTo(company_2.getLogoImgKey());
    }

    @Test
    void 회사_정보_수정_성공() {
        //given
        Member member = saveMember();
        Company company = saveCompany(member);

        String updateName = "updatedName";
        String updateAddress = "updatedAddress";

        //when
        companyService.updateCompanyInfo(member.getId(), company.getId(), new UpdateCompanyReq(updateName, updateAddress));

        //then
        Company findCompany = companyRepository.findById(company.getId()).get();
        assertThat(findCompany.getName()).isEqualTo(updateName);
        assertThat(findCompany.getAddress()).isEqualTo(updateAddress);
    }

    @Test
    void 회사_로고_이미지_수정_성공() {
        //given
        Member member = saveMember();
        Company company = saveCompany(member);

        MockMultipartFile newLogoImg = new MockMultipartFile(
                "logo", "newLogo.png", "image/png", "test data 2".getBytes());
        String newLogoImgKey = "new_logo_img_key";
        when(s3Service.uploadFile(newLogoImg, S3UploadTarget.COMPANY_LOGO))
                .thenReturn(newLogoImgKey);

        //when
        companyService.updateCompanyLogoImg(member.getId(), company.getId(), newLogoImg);

        //then
        Company findCompany = companyRepository.findById(company.getId()).get();
        assertThat(findCompany.getLogoImgKey()).isEqualTo(newLogoImgKey);
    }

    @Test
    void 회사_삭제_성공() {
        //given
        Member member = saveMember();
        Company company = saveCompany(member);

        //when
        companyService.deleteCompany(member.getId(), company.getId());

        //then
        List<Company> all = companyRepository.findAll();
        assertThat(all).isEmpty();
    }
}
