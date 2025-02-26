package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.req.CreateBmReq;
import com.pitchain.dto.req.UpdateBmReq;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.dto.res.PtImgRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.MyBm;
import com.pitchain.entity.PtImg;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.MemberRepository;
import com.pitchain.repository.MyBmHistoryRepository;
import com.pitchain.repository.MyBmRepository;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private MemberRepository memberRepository;
    @Autowired
    private MyBmRepository myBmRepository;
    @Autowired
    private MyBmHistoryRepository myBmHistoryRepository;

    private static final String NAME = "bm_name";
    private static final MainCategory MAIN_CATEGORY = MainCategory.FOOD;
    private static final List<SubCategory> SUB_CATEGORIES = List.of(SubCategory.BEVERAGE_COFFEE, SubCategory.ALCOHOL);
    private static final String COMPANY = "bm_company";
    private static final String LOGO_IMG_KEY = "bm_logo_img_key";
    private static final String INTRO = "bm_intro";
    private static final String DESCRIPTION = "bm_description";
    private static final String DESC_IMG_KEY = "bm_desc_img_key";
    private static final String ADDRESS = "bm_address";
    private static final Long VALUATION_CAP = 100000L;
    private static final Long GOAL_INVESTMENT = 200000L;
    private static final Integer MAX_ISSUED_SHARE = 1000;
    private static final LocalDate DEADLINE = LocalDate.now();
    private static final String LONG_PITCH_URL = "bm_long_pitch_url";

    private static final MockMultipartFile LOGO_IMG = new MockMultipartFile(
            "logo", "logo.png", "image/png", "test data 1".getBytes());
    private static final MockMultipartFile DESC_IMG = new MockMultipartFile(
            "description", "description.png", "image/png", "test data 2".getBytes());

    private Member saveMember() {
        return memberRepository.save(new Member(Country.ROK, Strings.EMPTY));
    }

    private Bm saveBm(Member member) {
        return bmRepository.save(new Bm(member, NAME, MAIN_CATEGORY, COMPANY, LOGO_IMG_KEY,
                INTRO, DESCRIPTION, DESC_IMG_KEY, ADDRESS, VALUATION_CAP,
                GOAL_INVESTMENT, MAX_ISSUED_SHARE, DEADLINE, LONG_PITCH_URL));
    }

    private List<PtImg> createPtImgs(Bm bm) {
        return List.of(
                new PtImg(bm, 1, "fake_01.jpg"),
                new PtImg(bm, 2, "fake_02.jpg"),
                new PtImg(bm, 3, "fake_03.jpg")
        );
    }

    @Test
    void BM_생성_성공() {
        //given
        Member member = saveMember();
        CreateBmReq createBmReq = new CreateBmReq(NAME, MAIN_CATEGORY, SUB_CATEGORIES, COMPANY,
                INTRO, DESCRIPTION, ADDRESS, VALUATION_CAP, GOAL_INVESTMENT, MAX_ISSUED_SHARE, DEADLINE, LONG_PITCH_URL);

        when(s3Service.uploadFile(LOGO_IMG, S3UploadTarget.COMPANY_LOGO))
                .thenReturn(LOGO_IMG_KEY);
        when(s3Service.uploadFile(DESC_IMG, S3UploadTarget.COMPANY_DESC))
                .thenReturn(DESC_IMG_KEY);

        //when
        bmService.createBm(member.getId(), createBmReq, LOGO_IMG, DESC_IMG);

        //then
        List<Bm> all = bmRepository.findAll();
        Bm bm = all.get(0);

        assertThat(bm.getName()).isEqualTo(NAME);
        assertThat(bm.getMainCategory()).isEqualTo(MAIN_CATEGORY);
        assertThat(bm.getKoreanSubCategories()).isEqualTo(SUB_CATEGORIES.stream().map(SubCategory::getKoreanName).toList());
        assertThat(bm.getCompany()).isEqualTo(COMPANY);
        assertThat(bm.getLogoImgKey()).isEqualTo(LOGO_IMG_KEY);
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
        Member member = saveMember();
        Bm bm = saveBm(member);

        bm.updateSubCategories(SUB_CATEGORIES);
        List<String> subCategories = bm.getKoreanSubCategories();

        List<PtImg> ptImgs = createPtImgs(bm);
        bm.updatePtImgs(ptImgs);
        List<PtImgRes> ptImgResList = ptImgs.stream()
                .map(ptImg -> PtImgRes.createRes(ptImg.getSerialNum(),
                        s3Service.getFileURL(ptImg.getImgKey())))
                .toList();

        //when
        BmDetailRes bmDetail = bmService.getBmDetail(member.getId(), bm.getId());

        //then
        assertThat(bmDetail.id()).isEqualTo(bm.getId());
        assertThat(bmDetail.name()).isEqualTo(bm.getName());
        assertThat(bmDetail.company()).isEqualTo(bm.getCompany());
        assertThat(bmDetail.intro()).isEqualTo(bm.getIntro());
        assertThat(bmDetail.mainCategory()).isEqualTo(bm.getMainCategory().getKoreanName());
        assertThat(bmDetail.logoImgURL()).isEqualTo(s3Service.getFileURL(bm.getLogoImgKey()));
        assertThat(bmDetail.description()).isEqualTo(bm.getDescription());
        assertThat(bmDetail.descImgURL()).isEqualTo(s3Service.getFileURL(bm.getDescImgKey()));
        assertThat(bmDetail.address()).isEqualTo(bm.getAddress());
        assertThat(bmDetail.createdAt()).isEqualTo(bm.getCreatedAt());
        assertThat(bmDetail.longPitchURL()).isEqualTo(bm.getLongPitchURL());
        assertThat(bmDetail.spURL()).isEqualTo(s3Service.getFileURL(bm.getSpKey()));
        assertThat(bmDetail.isLiked()).isFalse();
        assertThat(bmDetail.likeCnt()).isEqualTo(0);
        assertThat(bmDetail.ptImgResList()).isEqualTo(ptImgResList);
        assertThat(bmDetail.subCategories()).isEqualTo(subCategories);

        assertThat(myBmHistoryRepository.findByMemberAndBm(member, bm).get()).isNotNull();
    }

    @Test
    void BM_조회_성공_좋아요_존재() {
        //given
        Member member_01 = saveMember();
        Member member_02 = saveMember();
        Bm bm = saveBm(member_01);
        myBmRepository.save(new MyBm(member_01, bm));
        myBmRepository.save(new MyBm(member_02, bm));

        bm.updateSubCategories(SUB_CATEGORIES);
        List<String> subCategories = bm.getKoreanSubCategories();

        List<PtImg> ptImgs = createPtImgs(bm);
        bm.updatePtImgs(ptImgs);
        List<PtImgRes> ptImgResList = ptImgs.stream()
                .map(ptImg -> PtImgRes.createRes(ptImg.getSerialNum(),
                        s3Service.getFileURL(ptImg.getImgKey())))
                .toList();

        //when
        BmDetailRes bmDetail = bmService.getBmDetail(member_01.getId(), bm.getId());

        //then
        assertThat(bmDetail.id()).isEqualTo(bm.getId());
        assertThat(bmDetail.name()).isEqualTo(bm.getName());
        assertThat(bmDetail.company()).isEqualTo(bm.getCompany());
        assertThat(bmDetail.intro()).isEqualTo(bm.getIntro());
        assertThat(bmDetail.mainCategory()).isEqualTo(bm.getMainCategory().getKoreanName());
        assertThat(bmDetail.logoImgURL()).isEqualTo(s3Service.getFileURL(bm.getLogoImgKey()));
        assertThat(bmDetail.description()).isEqualTo(bm.getDescription());
        assertThat(bmDetail.descImgURL()).isEqualTo(s3Service.getFileURL(bm.getDescImgKey()));
        assertThat(bmDetail.address()).isEqualTo(bm.getAddress());
        assertThat(bmDetail.createdAt()).isEqualTo(bm.getCreatedAt());
        assertThat(bmDetail.longPitchURL()).isEqualTo(bm.getLongPitchURL());
        assertThat(bmDetail.spURL()).isEqualTo(s3Service.getFileURL(bm.getSpKey()));
        assertThat(bmDetail.isLiked()).isTrue();
        assertThat(bmDetail.likeCnt()).isEqualTo(2);
        assertThat(bmDetail.ptImgResList()).isEqualTo(ptImgResList);
        assertThat(bmDetail.subCategories()).isEqualTo(subCategories);

        assertThat(myBmHistoryRepository.findByMemberAndBm(member_01, bm).get()).isNotNull();
    }

    @Test
    void BM_조회_실패() {
        //given
        Member member = saveMember();
        Long invalidId = Long.MAX_VALUE;

        //when
        GeneralHandler e_1 = assertThrows(GeneralHandler.class, () -> bmService.getBmDetail(invalidId, invalidId));
        GeneralHandler e_2 = assertThrows(GeneralHandler.class, () -> bmService.getBmDetail(member.getId(), invalidId));

        //then
        assertThat(e_1.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
        assertThat(e_2.getErrorStatus()).isEqualTo(ErrorStatus.BM_NOT_FOUND);
    }

    @Test
    void BM_수정_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        final String updatedName = "updated_bm_name";
        final MainCategory updatedMainCategory = MainCategory.COMMUNICATION_SECURITY_DATA;
        final List<SubCategory> updatedSubCategories = List.of(SubCategory.DATA_ANALYTICS, SubCategory.CYBER_SECURITY);
        final String updatedCompany = "updated_bm_company";
        final String updatedLogoImgKey = "updated_bm_logo_img_key";
        final String updatedIntro = "updated_bm_intro";
        final String updatedDescription = "updated_bm_description";
        final String updatedDescImgKey = "updated_bm_desc_img_key";
        final String updatedAddress = "updated_bm_address";
        final Long updatedValuationCap = 200000L;
        final Long updatedGoalInvestment = 2000L;
        final Integer updatedMaxIssuedShare = 2000;
        final LocalDate updatedDeadline = LocalDate.now().plusDays(30);
        final String updatedLongPitchURL = "updated_bm_long_pitch_url";

        UpdateBmReq updateBmReq = new UpdateBmReq(updatedName, updatedMainCategory,
                updatedSubCategories, updatedCompany, updatedIntro, updatedDescription,
                updatedAddress, updatedValuationCap, updatedGoalInvestment,
                updatedMaxIssuedShare, updatedDeadline, updatedLongPitchURL
        );

        when(s3Service.uploadFile(LOGO_IMG, S3UploadTarget.COMPANY_LOGO))
                .thenReturn(updatedLogoImgKey);
        when(s3Service.uploadFile(DESC_IMG, S3UploadTarget.COMPANY_DESC))
                .thenReturn(updatedDescImgKey);

        //when
        bmService.updateBm(member.getId(), bm.getId(), updateBmReq, LOGO_IMG, DESC_IMG);

        //then
        Bm updatedBm = bmRepository.findById(bm.getId()).orElseThrow();
        assertThat(updatedBm.getName()).isEqualTo(updatedName);
        assertThat(updatedBm.getMainCategory()).isEqualTo(updatedMainCategory);
        assertThat(updatedBm.getKoreanSubCategories()).isEqualTo(updatedSubCategories.stream().map(SubCategory::getKoreanName).toList());
        assertThat(updatedBm.getCompany()).isEqualTo(updatedCompany);
        assertThat(updatedBm.getLogoImgKey()).isEqualTo(updatedLogoImgKey);
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
        Member member_01 = saveMember();
        Bm bm = saveBm(member_01);

        final String UPDATED_NAME = "update_bm_name";
        final MainCategory UPDATED_MAIN_CATEGORY = MainCategory.COMMUNICATION_SECURITY_DATA;
        final List<SubCategory> UPDATED_SUB_CATEGORIES = List.of(SubCategory.DATA_ANALYTICS, SubCategory.CYBER_SECURITY);
        final String UPDATED_COMPANY = "update_bm_company";
        final String UPDATED_INTRO = "update_bm_intro";
        final String UPDATED_DESCRIPTION = "update_bm_description";
        final String UPDATED_ADDRESS = "update_bm_address";
        final Long UPDATED_VALUATION_CAP = 200000L;
        final Long UPDATED_GOAL_INVESTMENT = 2000L;
        final Integer UPDATED_MAX_ISSUED_SHARE = 2000;
        final LocalDate UPDATED_DEADLINE = LocalDate.now().plusDays(30);
        final String UPDATED_LONG_PITCH_URL = "updated_bm_long_pitch_url";

        UpdateBmReq updateBmReq = new UpdateBmReq(UPDATED_NAME, UPDATED_MAIN_CATEGORY,
                UPDATED_SUB_CATEGORIES, UPDATED_COMPANY, UPDATED_INTRO, UPDATED_DESCRIPTION,
                UPDATED_ADDRESS, UPDATED_VALUATION_CAP, UPDATED_GOAL_INVESTMENT,
                UPDATED_MAX_ISSUED_SHARE, UPDATED_DEADLINE, UPDATED_LONG_PITCH_URL
        );

        Long invalidId = Long.MAX_VALUE;
        Member member_02 = saveMember();

        //when
        GeneralHandler e_1 = assertThrows(GeneralHandler.class, () -> bmService.updateBm(invalidId, bm.getId(), updateBmReq, null, null));
        GeneralHandler e_2 = assertThrows(GeneralHandler.class, () -> bmService.updateBm(member_01.getId(), invalidId, updateBmReq, null, null));
        GeneralHandler e_3 = assertThrows(GeneralHandler.class, () -> bmService.updateBm(member_02.getId(), bm.getId(), updateBmReq, null, null));

        //then
        assertThat(e_1.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
        assertThat(e_2.getErrorStatus()).isEqualTo(ErrorStatus.BM_NOT_FOUND);
        assertThat(e_3.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
    }

    @Test
    void BM_PT_IMG_추가_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        List<MultipartFile> ptImgs = List.of(
                new MockMultipartFile("img_1", "img_1.png", "image/png", "img1".getBytes()),
                new MockMultipartFile("img_2", "img_2.png", "image/png", "img2".getBytes()),
                new MockMultipartFile("img_3", "img_3.png", "image/png", "img3".getBytes())
        );

        for (int i = 0; i < ptImgs.size(); i++) {
            String fakeUrl = "fake_" + (i + 1);
            when(s3Service.uploadFile(ptImgs.get(i), S3UploadTarget.COMPANY_PT))
                    .thenReturn(fakeUrl);
        }

        //when
        bmService.updatePtImgs(member.getId(), bm.getId(), ptImgs);

        //then
        Bm updatedBm = bmRepository.findById(bm.getId()).orElseThrow();
        List<PtImg> updatedPtImgs = updatedBm.getPtImgs();

        assertThat(updatedPtImgs).hasSize(ptImgs.size());
        for (int i = 0; i < ptImgs.size(); i++) {
            assertThat(updatedPtImgs.get(i).getSerialNum()).isEqualTo(i);
            assertThat(updatedPtImgs.get(i).getBm().getId()).isEqualTo(updatedBm.getId());
            assertThat(updatedPtImgs.get(i).getImgKey()).isEqualTo("fake_" + (i + 1));
        }
    }

    @Test
    void BM_PT_IMG_수정_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);
        bm.updatePtImgs(List.of(
                new PtImg(bm, 0, "origin_img_0.png"),
                new PtImg(bm, 1, "origin_img_1.png")
        ));

        List<MultipartFile> updatePtImgs = List.of(
                new MockMultipartFile("img_1", "img_1.png", "image/png", "img1".getBytes()),
                new MockMultipartFile("img_2", "img_2.png", "image/png", "img2".getBytes()),
                new MockMultipartFile("img_3", "img_3.png", "image/png", "img3".getBytes())
        );
        for (int i = 0; i < updatePtImgs.size(); i++) {
            String fakeUrl = "fake_" + (i + 1);
            when(s3Service.uploadFile(updatePtImgs.get(i), S3UploadTarget.COMPANY_PT))
                    .thenReturn(fakeUrl);
        }

        //when
        bmService.updatePtImgs(member.getId(), bm.getId(), updatePtImgs);

        //then
        Bm updatedBm = bmRepository.findById(bm.getId()).orElseThrow();
        List<PtImg> updatedPtImgs = updatedBm.getPtImgs();

        assertThat(updatedPtImgs).hasSize(updatePtImgs.size());
        for (int i = 0; i < updatePtImgs.size(); i++) {
            assertThat(updatedPtImgs.get(i).getSerialNum()).isEqualTo(i);
            assertThat(updatedPtImgs.get(i).getBm().getId()).isEqualTo(updatedBm.getId());
            assertThat(updatedPtImgs.get(i).getImgKey()).isEqualTo("fake_" + (i + 1));
        }
    }

    @Test
    void BM_PT_IMG_수정_실패() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        Long invalidId = Long.MAX_VALUE;
        Member member_02 = saveMember();

        //when
        bmService.updatePtImgs(member.getId(), bm.getId(), null);
        GeneralHandler e_1 = assertThrows(GeneralHandler.class, () -> bmService.updatePtImgs(invalidId, bm.getId(), null));
        GeneralHandler e_2 = assertThrows(GeneralHandler.class, () -> bmService.updatePtImgs(member.getId(), invalidId, null));
        GeneralHandler e_3 = assertThrows(GeneralHandler.class, () -> bmService.updatePtImgs(member_02.getId(), bm.getId(), null));

        //then
        assertThat(e_1.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
        assertThat(e_2.getErrorStatus()).isEqualTo(ErrorStatus.BM_NOT_FOUND);
        assertThat(e_3.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
    }

    @Test
    void BM_삭제_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        //when
        bmService.deleteBm(member.getId(), bm.getId());

        //then
        List<Bm> all = bmRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    void BM_삭제_실패() {
        //given
        Member member_01 = saveMember();
        Bm bm = saveBm(member_01);

        Long invalidId = Long.MAX_VALUE;
        Member member_02 = saveMember();

        //when
        GeneralHandler e_1 = assertThrows(GeneralHandler.class, () -> bmService.deleteBm(invalidId, bm.getId()));
        GeneralHandler e_2 = assertThrows(GeneralHandler.class, () -> bmService.deleteBm(member_01.getId(), invalidId));
        GeneralHandler e_3 = assertThrows(GeneralHandler.class, () -> bmService.deleteBm(member_02.getId(), bm.getId()));

        //then
        assertThat(e_1.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
        assertThat(e_2.getErrorStatus()).isEqualTo(ErrorStatus.BM_NOT_FOUND);
        assertThat(e_3.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
    }
}
