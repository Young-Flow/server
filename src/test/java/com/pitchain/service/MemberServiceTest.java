package com.pitchain.service;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.dto.req.CompanyUpdateReq;
import com.pitchain.dto.req.IndividualUpdateReq;
import com.pitchain.dto.res.CompanyProfileRes;
import com.pitchain.dto.res.IndividualProfileRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Company;
import com.pitchain.entity.Individual;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.CompanyRepository;
import com.pitchain.repository.IndividualRepository;
import com.pitchain.repository.MemberRepository;
import com.pitchain.util.EntitySaver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @MockBean
    private S3Service s3Service;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private EntitySaver entitySaver;

    @Test
    void 내_정보_조회_성공_개인인_경우() {
        //given
        Member individualMember = entitySaver.saveIndividualMember();
        Individual individual = entitySaver.saveIndividual(individualMember);
        Member member = individual.getMember();
        MemberDetails individualMemberDetails = createIndividualMemberMemberDetails(member);

        //when
        IndividualProfileRes individualDetailRes = (IndividualProfileRes) memberService.getMyProfile(individualMemberDetails);
        String profileImgURL = s3Service.getFileURL(member.getProfileImgKey());

        //then
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(1);

        Member findMember = members.get(0);
        Individual findIndividual = individualRepository.findByMemberId(findMember.getId()).orElseThrow();
        assertThat(individualDetailRes.getProfileImgURL()).isEqualTo(profileImgURL);
        assertThat(individualDetailRes.getName()).isEqualTo(findMember.getName());
        assertThat(individualDetailRes.getEmail()).isEqualTo(findMember.getEmail());
        assertThat(individualDetailRes.getOauthProvider()).isEqualTo(findIndividual.getOauthProvider());
        assertThat(individualDetailRes.getMemberRole()).isEqualTo(findMember.getRole());
    }

    @Test
    void 내_정보_조회_성공_회사인_경우() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);

        Member member = company.getMember();
        MemberDetails companyMemberDetails = createCompanyMemberDetails(company);

        //when
        CompanyProfileRes companyProfileRes = (CompanyProfileRes) memberService.getMyProfile(companyMemberDetails);
        String profileImgURL = s3Service.getFileURL(member.getProfileImgKey());

        //then
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(1);

        Member findMember = members.get(0);
        Company foundCompany = companyRepository.findByMemberId(findMember.getId()).orElseThrow();
        assertThat(companyProfileRes.getMemberRole()).isEqualTo(findMember.getRole());
        assertThat(companyProfileRes.getAddress()).isEqualTo(foundCompany.getAddress());
        assertThat(companyProfileRes.getProfileImgURL()).isEqualTo(profileImgURL);
        assertThat(companyProfileRes.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 이메일_중복_확인_중복인_경우() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Member member = company.getMember();

        //when
        boolean isDuplicated = memberService.isDuplicatedEmail(member.getEmail());

        //then
        assertThat(isDuplicated).isTrue();
    }

    @Test
    void 이메일_중복_확인_중복이_아닌_경우() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);

        String newEmail = "newEmail";

        //when
        boolean isDuplicated = memberService.isDuplicatedEmail(newEmail);

        //then
        assertThat(isDuplicated).isFalse();
    }

    @Test
    void 나의_정보_수정_성공_회사인_경우() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);

        MemberDetails companyMemberDetails = createCompanyMemberDetails(company);

        CompanyUpdateReq baseUpdateMemberReq = new CompanyUpdateReq("address", MemberRole.COMPANY);

        //when
        memberService.updateMyProfile(companyMemberDetails, baseUpdateMemberReq);

        //then
        assertThat(company.getAddress()).isEqualTo(baseUpdateMemberReq.getAddress());
    }

    @Test
    void 나의_정보_수정_성공_개인인_경우() {
        //given
        Member individualMember = entitySaver.saveIndividualMember();
        Individual individual = entitySaver.saveIndividual(individualMember);
        Member member = individual.getMember();

        MemberDetails individualMemberDetails = createIndividualMemberMemberDetails(member);

        IndividualUpdateReq updateMemberReq = new IndividualUpdateReq("newName", MemberRole.INDIVIDUAL);

        //when
        memberService.updateMyProfile(individualMemberDetails, updateMemberReq);

        //then
        Member findMember = memberRepository.findById(individual.getMember().getId()).orElseThrow();
        assertThat(findMember.getName()).isEqualTo(updateMemberReq.getName());
    }

    @Test
    void 프로필_이미지_수정_성공() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);

        MemberDetails companyMemberDetails = createCompanyMemberDetails(company);

        MockMultipartFile newLogoImg = new MockMultipartFile(
                "logo", "newLogo.png", "image/png", "test data 2".getBytes());
        String newLogoImgKey = "new_logo_img_key";
        when(s3Service.uploadFile(newLogoImg, S3UploadTarget.MEMBER_PROFILE))
                .thenReturn(newLogoImgKey);

        //when
        memberService.updateProfileImg(companyMemberDetails, newLogoImg);

        //then
        Member findMember = memberRepository.findById(company.getMember().getId()).orElseThrow();
        assertThat(findMember.getProfileImgKey()).isEqualTo(newLogoImgKey);
    }


    private MemberDetails createIndividualMemberMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }

    private MemberDetails createCompanyMemberDetails(Company company) {
        return new MemberDetails(company.getMember().getId(), MemberRole.COMPANY);
    }


}
