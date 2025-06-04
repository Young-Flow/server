package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.dto.req.BmCreateReq;
import com.pitchain.dto.req.BmUpdateReq;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.entity.*;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.BmScrapRepository;
import com.pitchain.util.EntitySaver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class BmServiceTest {
    @MockBean
    private S3Service s3Service;
    @Autowired
    private BmService bmService;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private BmScrapRepository bmScrapRepository;
    @Autowired
    private EntitySaver entitySaver;

    private static final String NAME = "bm_name";
    private static final MainCategory MAIN_CATEGORY = MainCategory.FOOD;
    private static final List<SubCategory> SUB_CATEGORIES = List.of(SubCategory.BEVERAGE_COFFEE, SubCategory.ALCOHOL);
    private static final String INTRO = "bm_intro";
    private static final String DESCRIPTION = "bm_description";
    private static final String DESC_IMG_KEY = "bm_desc_img_key";
    private static final String ADDRESS = "bm_address";
    private static final Long VALUATION_CAP = 100000L;
    private static final Long GOAL_INVESTMENT = 200000L;
    private static final Integer MAX_ISSUED_SHARE = 1000;
    private static final LocalDate DEADLINE = LocalDate.now();
    private static final String LONG_PITCH_URL = "bm_long_pitch_url";

    private static final MockMultipartFile DESC_IMG = new MockMultipartFile(
            "description", "description.png", "image/png", "test data 2".getBytes());

    private MemberDetails createIndividualMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }


    private MemberDetails createCompanyMemberDetails(Company company) {
        return new MemberDetails(company.getMember().getId(), MemberRole.COMPANY);
    }

    @Test
    void BM_생성_성공() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        MemberDetails companyMemberDetails = createCompanyMemberDetails(company);

        BmCreateReq bmCreateReq = new BmCreateReq(company.getId(), NAME, MAIN_CATEGORY, SUB_CATEGORIES,
                INTRO, DESCRIPTION, ADDRESS, VALUATION_CAP, GOAL_INVESTMENT, MAX_ISSUED_SHARE, DEADLINE, LONG_PITCH_URL);

        when(s3Service.uploadFile(DESC_IMG, S3UploadTarget.COMPANY_DESC))
                .thenReturn(DESC_IMG_KEY);

        //when
        bmService.createBm(companyMemberDetails, bmCreateReq, DESC_IMG);

        //then
        List<Bm> all = bmRepository.findAll();
        Bm bm = all.get(0);

        assertThat(bm.getName()).isEqualTo(NAME);
        assertThat(bm.getMainCategory()).isEqualTo(MAIN_CATEGORY);
        assertThat(bm.getIntro()).isEqualTo(INTRO);
        assertThat(bm.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(bm.getDescImgKey()).isEqualTo(DESC_IMG_KEY);
        assertThat(bm.getAddress()).isEqualTo(ADDRESS);
        assertThat(bm.getValuationCap()).isEqualTo(VALUATION_CAP);
        assertThat(bm.getGoalInvestment()).isEqualTo(GOAL_INVESTMENT);
        assertThat(bm.getMaxIssuedShare()).isEqualTo(MAX_ISSUED_SHARE);
        assertThat(bm.getDeadline()).isEqualTo(DEADLINE);
        assertThat(bm.getLongPitchURL()).isEqualTo(LONG_PITCH_URL);
    }

    @Test
    void BM_조회_성공_좋아요_없음() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);

        //when
        MemberDetails memberDetails = createCompanyMemberDetails(company);
        BmDetailRes bmDetail = bmService.getBmDetail(memberDetails, bm.getId());

        //then
        assertThat(bmDetail.companyId()).isEqualTo(company.getId());
        assertThat(bmDetail.companyProfileImgURL()).isEqualTo(company.getMember().getProfileImgKey());
        assertThat(bmDetail.companyName()).isEqualTo(company.getMember().getName());
        assertThat(bmDetail.companyAddress()).isEqualTo(company.getAddress());

        assertThat(bmDetail.bmId()).isEqualTo(bm.getId());
        assertThat(bmDetail.bmName()).isEqualTo(bm.getName());
        assertThat(bmDetail.intro()).isEqualTo(bm.getIntro());
        assertThat(bmDetail.mainCategory()).isEqualTo(bm.getMainCategory().getKoreanName());
        assertThat(bmDetail.description()).isEqualTo(bm.getDescription());
        assertThat(bmDetail.descImgURL()).isEqualTo(bm.getDescImgKey());
        assertThat(bmDetail.bmAddress()).isEqualTo(bm.getAddress());
        assertThat(bmDetail.createdAt()).isEqualTo(bm.getCreatedAt());
        assertThat(bmDetail.longPitchURL()).isEqualTo(bm.getLongPitchURL());
        assertThat(bmDetail.isScraped()).isFalse();
    }

    @Test
    void BM_조회_성공_좋아요_존재() {
        //given
        Member individual_01 = entitySaver.saveIndividualMember();
        Member individual_02 = entitySaver.saveIndividualMember();

        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);

        bmScrapRepository.save(new BmScrap(individual_01, bm));
        bmScrapRepository.save(new BmScrap(individual_02, bm));

        //when
        MemberDetails individualMemberDetails = createIndividualMemberDetails(individual_01);
        BmDetailRes bmDetail = bmService.getBmDetail(individualMemberDetails, bm.getId());

        //then
        assertThat(bmDetail.companyId()).isEqualTo(company.getId());
        assertThat(bmDetail.companyProfileImgURL()).isEqualTo(company.getMember().getProfileImgKey());
        assertThat(bmDetail.companyName()).isEqualTo(company.getMember().getName());
        assertThat(bmDetail.companyAddress()).isEqualTo(company.getAddress());

        assertThat(bmDetail.bmId()).isEqualTo(bm.getId());
        assertThat(bmDetail.bmName()).isEqualTo(bm.getName());
        assertThat(bmDetail.intro()).isEqualTo(bm.getIntro());
        assertThat(bmDetail.mainCategory()).isEqualTo(bm.getMainCategory().getKoreanName());
        assertThat(bmDetail.description()).isEqualTo(bm.getDescription());
        assertThat(bmDetail.descImgURL()).isEqualTo(bm.getDescImgKey());
        assertThat(bmDetail.bmAddress()).isEqualTo(bm.getAddress());
        assertThat(bmDetail.createdAt()).isEqualTo(bm.getCreatedAt());
        assertThat(bmDetail.longPitchURL()).isEqualTo(bm.getLongPitchURL());
        assertThat(bmDetail.isScraped()).isTrue();
        assertThat(bmDetail.scrapCnt()).isEqualTo(2);
    }

    @Test
    void BM_조회_실패() {
        //given
        Member individual = entitySaver.saveIndividualMember();

        Long invalidId = Long.MAX_VALUE;
        MemberDetails memberDetails = createIndividualMemberDetails(individual);
        MemberDetails invalidMemberDetails = new MemberDetails(Long.MAX_VALUE, MemberRole.INDIVIDUAL);

        //when
        GeneralException e_1 = assertThrows(GeneralException.class, () -> bmService.getBmDetail(invalidMemberDetails, invalidId));
        GeneralException e_2 = assertThrows(GeneralException.class, () -> bmService.getBmDetail(memberDetails, invalidId));

        //then
        assertThat(e_1.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
        assertThat(e_2.getErrorStatus()).isEqualTo(ErrorStatus.BM_NOT_FOUND);
    }

    @Test
    void BM_수정_성공() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);

        MemberDetails companyMemberDetails = createCompanyMemberDetails(company);

        final String updatedName = "updated_bm_name";
        final MainCategory updatedMainCategory = MainCategory.COMMUNICATION_SECURITY_DATA;
        final List<SubCategory> updatedSubCategories = List.of(SubCategory.DATA_ANALYTICS, SubCategory.CYBER_SECURITY);
        final String updatedIntro = "updated_bm_intro";
        final String updatedDescription = "updated_bm_description";
        final String updatedDescImgKey = "updated_bm_desc_img_key";
        final String updatedAddress = "updated_bm_address";
        final Long updatedValuationCap = 200000L;
        final Long updatedGoalInvestment = 2000L;
        final Integer updatedMaxIssuedShare = 2000;
        final LocalDate updatedDeadline = LocalDate.now().plusDays(30);
        final String updatedLongPitchURL = "updated_bm_long_pitch_url";

        BmUpdateReq bmUpdateReq = new BmUpdateReq(updatedName, updatedMainCategory,
                updatedSubCategories, updatedIntro, updatedDescription, updatedAddress, updatedValuationCap,
                updatedGoalInvestment, updatedMaxIssuedShare, updatedDeadline, updatedLongPitchURL
        );

        when(s3Service.uploadFile(DESC_IMG, S3UploadTarget.COMPANY_DESC))
                .thenReturn(updatedDescImgKey);

        //when
        bmService.updateBm(companyMemberDetails, bm.getId(), bmUpdateReq, DESC_IMG);

        //then
        Bm updatedBm = bmRepository.findById(bm.getId()).orElseThrow();

        assertThat(updatedBm.getName()).isEqualTo(updatedName);
        assertThat(updatedBm.getMainCategory()).isEqualTo(updatedMainCategory);
        assertThat(updatedBm.getIntro()).isEqualTo(updatedIntro);
        assertThat(updatedBm.getDescription()).isEqualTo(updatedDescription);
        assertThat(updatedBm.getDescImgKey()).isEqualTo(updatedDescImgKey);
        assertThat(updatedBm.getAddress()).isEqualTo(updatedAddress);
        assertThat(updatedBm.getValuationCap()).isEqualTo(updatedValuationCap);
        assertThat(updatedBm.getGoalInvestment()).isEqualTo(updatedGoalInvestment);
        assertThat(updatedBm.getMaxIssuedShare()).isEqualTo(updatedMaxIssuedShare);
        assertThat(updatedBm.getDeadline()).isEqualTo(updatedDeadline);
        assertThat(updatedBm.getLongPitchURL()).isEqualTo(updatedLongPitchURL);
    }

    @Test
    void BM_수정_실패() {
        //given
        Member companyMember_1 = entitySaver.saveCompanyMember();
        Company company_1 = entitySaver.saveCompany(companyMember_1);
        Bm bm = entitySaver.saveBm(company_1);

        MemberDetails companyMemberDetails_01 = createCompanyMemberDetails(company_1);

        final String UPDATED_NAME = "update_bm_name";
        final MainCategory UPDATED_MAIN_CATEGORY = MainCategory.COMMUNICATION_SECURITY_DATA;
        final List<SubCategory> UPDATED_SUB_CATEGORIES = List.of(SubCategory.DATA_ANALYTICS, SubCategory.CYBER_SECURITY);
        final String UPDATED_INTRO = "update_bm_intro";
        final String UPDATED_DESCRIPTION = "update_bm_description";
        final String UPDATED_ADDRESS = "update_bm_address";
        final Long UPDATED_VALUATION_CAP = 200000L;
        final Long UPDATED_GOAL_INVESTMENT = 2000L;
        final Integer UPDATED_MAX_ISSUED_SHARE = 2000;
        final LocalDate UPDATED_DEADLINE = LocalDate.now().plusDays(30);
        final String UPDATED_LONG_PITCH_URL = "updated_bm_long_pitch_url";

        BmUpdateReq bmUpdateReq = new BmUpdateReq(UPDATED_NAME, UPDATED_MAIN_CATEGORY,
                UPDATED_SUB_CATEGORIES, UPDATED_INTRO, UPDATED_DESCRIPTION,
                UPDATED_ADDRESS, UPDATED_VALUATION_CAP, UPDATED_GOAL_INVESTMENT,
                UPDATED_MAX_ISSUED_SHARE, UPDATED_DEADLINE, UPDATED_LONG_PITCH_URL
        );

        Long invalidId = Long.MAX_VALUE;

        Member companyMember_2 = entitySaver.saveCompanyMember();
        Company company_2 = entitySaver.saveCompany(companyMember_2);
        MemberDetails companyMemberDetails_02 = createCompanyMemberDetails(company_2);

        Member individual = entitySaver.saveIndividualMember();
        MemberDetails individualMemberDetails = createIndividualMemberDetails(individual);

        //when
        GeneralException e_1 = assertThrows(GeneralException.class, () -> bmService.updateBm(individualMemberDetails, bm.getId(), bmUpdateReq, null));
        GeneralException e_2 = assertThrows(GeneralException.class, () -> bmService.updateBm(companyMemberDetails_01, invalidId, bmUpdateReq, null));
        GeneralException e_3 = assertThrows(GeneralException.class, () -> bmService.updateBm(companyMemberDetails_02, bm.getId(), bmUpdateReq, null));

        //then
        assertThat(e_1.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
        assertThat(e_2.getErrorStatus()).isEqualTo(ErrorStatus.BM_NOT_FOUND);
        assertThat(e_3.getErrorStatus()).isEqualTo(ErrorStatus.COMPANY_FORBIDDEN);
    }

    @Test
    void BM_삭제_성공() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);

        MemberDetails companyMemberDetails = createCompanyMemberDetails(company);

        //when
        bmService.deleteBm(companyMemberDetails, bm.getId());

        //then
        List<Bm> all = bmRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    void BM_삭제_실패() {
        //given
        Member companyMember_1 = entitySaver.saveCompanyMember();
        Company company_1 = entitySaver.saveCompany(companyMember_1);
        MemberDetails companyMemberDetails_1 = createCompanyMemberDetails(company_1);
        Bm bm = entitySaver.saveBm(company_1);

        Member companyMember_2 = entitySaver.saveCompanyMember();
        Company company_2 = entitySaver.saveCompany(companyMember_2);
        MemberDetails companyMemberDetails_2 = createCompanyMemberDetails(company_2);

        Long invalidId = Long.MAX_VALUE;
        Member individual = entitySaver.saveIndividualMember();
        MemberDetails individualMemberDetails = createIndividualMemberDetails(individual);

        //when
        GeneralException e_1 = assertThrows(GeneralException.class, () -> bmService.deleteBm(individualMemberDetails, bm.getId()));
        GeneralException e_2 = assertThrows(GeneralException.class, () -> bmService.deleteBm(companyMemberDetails_1, invalidId));
        GeneralException e_3 = assertThrows(GeneralException.class, () -> bmService.deleteBm(companyMemberDetails_2, bm.getId()));

        //then
        assertThat(e_1.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
        assertThat(e_2.getErrorStatus()).isEqualTo(ErrorStatus.BM_NOT_FOUND);
        assertThat(e_3.getErrorStatus()).isEqualTo(ErrorStatus.COMPANY_FORBIDDEN);
    }
}
