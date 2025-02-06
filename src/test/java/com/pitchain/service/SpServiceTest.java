package com.pitchain.service;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.dto.req.CreateSpReq;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.entity.*;
import com.pitchain.repository.*;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class SpServiceTest {

    @MockBean
    private S3Service s3Service;

    @Autowired
    private SpService spService;
    @Autowired
    private SpRepository spRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private MyBmRepository myBmRepository;
    @Autowired
    private CategoryPrefRepository categoryPrefRepository;

    private Member saveMember() {
        return memberRepository.save(new Member("member_name", UUID.randomUUID().toString(), Country.ROK, Strings.EMPTY));
    }

    private Bm saveBm(Member member) {
        return bmRepository.save(new Bm(member, "bm_name", MainCategory.FOOD, "bm_company", "bm_logo_img_key",
                "bm_intro", "bm_description", "bm_desc_img_key", "bm_address", 100000L,
                1000L, 1000, LocalDate.now(), "bm_long_pitch_url"));
    }

    private Bm saveBmWithMainCategory(Member member, MainCategory mainCategory) {
        return bmRepository.save(new Bm(member, "bm_name", mainCategory, "bm_company", "bm_logo_img_key",
                "bm_intro", "bm_description", "bm_desc_img_key", "bm_address", 100000L,
                1000L, 1000, LocalDate.now(), "bm_long_pitch_url"));
    }

    private Sp saveSp(Bm bm) {
        return spRepository.save(
                new Sp(bm, SP_KEY, THUMBNAIL_IMG.getOriginalFilename(), SP_NAME));
    }

    @BeforeEach
    void setUp() {
        member = saveMember();
        bm = saveBm(member);
    }

    private Member member;
    private Bm bm;

    private static final String SP_NAME = "sp_name";
    private static final String SP_KEY = "sp_vid.m3u8";
    private static final MockMultipartFile SP_VID = new MockMultipartFile(
            "sp_vid", "sp_vid.mp4", "video/mp4", "test data 1".getBytes());
    private static final MockMultipartFile THUMBNAIL_IMG = new MockMultipartFile(
            "thumbnail_img", "thumbnail_img.png", "image/png", "test data 2".getBytes());
    private static final List<SubCategory> SUB_CATEGORIES = List.of(SubCategory.BEVERAGE_COFFEE, SubCategory.ALCOHOL);

    @Test
    void SP_생성_성공() {
        //given
        CreateSpReq createSpReq = new CreateSpReq(bm.getId(), SP_NAME);

        when(s3Service.uploadFile(SP_VID, S3UploadTarget.COMPANY_VIDEO))
                .thenReturn(SP_VID.getOriginalFilename());
        when(s3Service.uploadFile(THUMBNAIL_IMG, S3UploadTarget.COMPANY_THUMBNAIL))
                .thenReturn(THUMBNAIL_IMG.getOriginalFilename());

        //when
        spService.createSp(member.getId(), createSpReq, SP_VID, THUMBNAIL_IMG);

        //then
        List<Sp> all = spRepository.findAll();
        Sp sp = all.get(0);
        assertThat(sp.getBm()).isEqualTo(bm);
        assertThat(sp.getSpKey()).isEqualTo(SP_KEY);
        assertThat(sp.getThumbnailImgKey()).isEqualTo(THUMBNAIL_IMG.getOriginalFilename());
        assertThat(sp.getName()).isEqualTo(SP_NAME);
    }

    @Test
    void SP_조회_성공_좋아요_없음() {
        //given
        Sp sp = saveSp(bm);

        bm.updateSubCategories(SUB_CATEGORIES);
        List<String> subCategories = bm.getKoreanSubCategories();

        //when
        SpDetailRes spDetail = spService.getSpDetail(member.getId(), sp.getId());

        //then
        assertThat(spDetail.bmId()).isEqualTo(sp.getBm().getId());
        assertThat(spDetail.spURL()).isEqualTo(s3Service.getFileURL(sp.getSpKey()));
        assertThat(spDetail.thumbnailImgURL()).isEqualTo(s3Service.getFileURL(sp.getThumbnailImgKey()));
        assertThat(spDetail.views()).isEqualTo(sp.getViews());
        assertThat(spDetail.name()).isEqualTo(sp.getName());
        assertThat(spDetail.logoImgURL()).isEqualTo(s3Service.getFileURL(sp.getBm().getLogoImgKey()));
        assertThat(spDetail.mainCategory()).isEqualTo(sp.getBm().getMainCategory().getKoreanName());
        assertThat(spDetail.subCategories()).isEqualTo(subCategories);
        assertThat(spDetail.company()).isEqualTo(sp.getBm().getCompany());
        assertThat(spDetail.isLiked()).isEqualTo(false);
        assertThat(spDetail.likeCnt()).isEqualTo(0L);
    }

    @Test
    void SP_조회_성공_좋아요_존재() {
        //given
        Member newMember = saveMember();
        Sp sp = saveSp(bm);
        myBmRepository.save(new MyBm(member, bm));
        myBmRepository.save(new MyBm(newMember, bm));

        bm.updateSubCategories(SUB_CATEGORIES);
        List<String> subCategories = bm.getKoreanSubCategories();

        //when
        SpDetailRes spDetail = spService.getSpDetail(member.getId(), sp.getId());

        //then
        assertThat(spDetail.bmId()).isEqualTo(sp.getBm().getId());
        assertThat(spDetail.spURL()).isEqualTo(s3Service.getFileURL(sp.getSpKey()));
        assertThat(spDetail.thumbnailImgURL()).isEqualTo(s3Service.getFileURL(sp.getThumbnailImgKey()));
        assertThat(spDetail.views()).isEqualTo(sp.getViews());
        assertThat(spDetail.name()).isEqualTo(sp.getName());
        assertThat(spDetail.logoImgURL()).isEqualTo(s3Service.getFileURL(sp.getBm().getLogoImgKey()));
        assertThat(spDetail.mainCategory()).isEqualTo(sp.getBm().getMainCategory().getKoreanName());
        assertThat(spDetail.subCategories()).isEqualTo(subCategories);
        assertThat(spDetail.company()).isEqualTo(sp.getBm().getCompany());
        assertThat(spDetail.isLiked()).isEqualTo(true);
        assertThat(spDetail.likeCnt()).isEqualTo(2L);
    }

    @Test
    void SP_전체_조회() {
        //given
        Sp sp_1 = saveSp(bm);

        Member newMember = saveMember();
        Bm newBm = saveBm(newMember);
        Sp sp_2 = saveSp(newBm);

        //when
        List<SpDetailRes> spDetails = spService.getSpDetails(member.getId());

        //then
        assertThat(spDetails).hasSize(2);
        boolean isContainBmIds = spDetails.stream()
                .map(SpDetailRes::bmId)
                .toList()
                .containsAll(
                        List.of(sp_1.getBm().getId(), sp_2.getBm().getId())
                );
        assertThat(isContainBmIds).isTrue();
    }

    @Test
    void SP_카테고리_필터링_조회() {
        //given
        Member member1 = saveMember();
        Member member2 = saveMember();
        Member member3 = saveMember();

        Bm bm1 = saveBmWithMainCategory(member1, MainCategory.TECH_DIGITAL);
        Bm bm2 = saveBmWithMainCategory(member2, MainCategory.TECH_DIGITAL);
        Bm bm3 = saveBmWithMainCategory(member3, MainCategory.FOOD);

        myBmRepository.save(new MyBm(member1, bm1));
        myBmRepository.save(new MyBm(member2, bm1));

        Sp sp1 = saveSp(bm1);
        Sp sp2 = saveSp(bm2);
        Sp sp3 = saveSp(bm3);

        //when
        List<SpDetailRes> spDetails = spService.getSpDetailsFilteredCategory(member1.getId(), MainCategory.TECH_DIGITAL.getKoreanName());

        // then
        assertThat(spDetails).hasSize(2);

        assertThat(spDetails.get(0).bmId()).isEqualTo(bm1.getId());
        assertThat(spDetails.get(0).mainCategory()).isEqualTo(bm1.getMainCategory().getKoreanName());
        assertThat(spDetails.get(0).name()).isEqualTo(sp1.getName());
        assertThat(spDetails.get(0).views()).isEqualTo(sp1.getViews());
        assertThat(spDetails.get(0).company()).isEqualTo(bm1.getCompany());
        assertThat(spDetails.get(0).isLiked()).isTrue();
        assertThat(spDetails.get(0).likeCnt()).isEqualTo(2);

        assertThat(spDetails.get(1).bmId()).isEqualTo(bm2.getId());
        assertThat(spDetails.get(1).mainCategory()).isEqualTo(bm2.getMainCategory().getKoreanName());
        assertThat(spDetails.get(1).name()).isEqualTo(sp2.getName());
        assertThat(spDetails.get(1).views()).isEqualTo(sp2.getViews());
        assertThat(spDetails.get(1).company()).isEqualTo(bm2.getCompany());
        assertThat(spDetails.get(1).isLiked()).isFalse();
        assertThat(spDetails.get(1).likeCnt()).isEqualTo(0);
    }

    @Test
    void AI로_추천받은_SP_조회() {
        //given
        Member member1 = saveMember();
        Member member2 = saveMember();
        Member member3 = saveMember();

        Bm bm1 = saveBmWithMainCategory(member1, MainCategory.TECH_DIGITAL);
        Bm bm2 = saveBmWithMainCategory(member2, MainCategory.TECH_DIGITAL);
        Bm bm3 = saveBmWithMainCategory(member3, MainCategory.FOOD);

        Sp sp1 = saveSp(bm1);
        Sp sp2 = saveSp(bm2);
        Sp sp3 = saveSp(bm3);

        //when
        List<SpDetailRes> spDetails = spService.getSpDetailsRecommendedFromAi(member1.getId(), List.of(bm1.getId(), bm2.getId(), bm3.getId()));

        //then
        assertThat(spDetails).hasSize(3);
        assertThat(spDetails.stream()
                .map(SpDetailRes::bmId)
                .toList())
                .containsExactlyInAnyOrder(bm1.getId(), bm2.getId(), bm3.getId());
    }

    @Test
    void 나의_선호_카테고리로_SP_조회() {
        //given
        Member member = saveMember();
        categoryPrefRepository.save(new CategoryPref(member, SubCategory.AI_ML));
        categoryPrefRepository.save(new CategoryPref(member, SubCategory.DATA_ANALYTICS));

        Member member1 = saveMember();
        Member member2 = saveMember();
        Member member3 = saveMember();

        Bm bm1 = saveBm(member1);
        bm1.addSubCategories(List.of(SubCategory.AI_ML, SubCategory.APP_DEVELOPMENT));
        Bm bm2 = saveBm(member2);
        bm2.addSubCategories(List.of(SubCategory.BLOCKCHAIN_WEB3, SubCategory.DATA_ANALYTICS));
        Bm bm3 = saveBm(member3);
        bm3.addSubCategories(List.of(SubCategory.BEVERAGE_COFFEE, SubCategory.ALCOHOL));

        saveSp(bm);
        Sp sp1 = saveSp(bm1);
        Sp sp2 = saveSp(bm2);
        Sp sp3 = saveSp(bm3);

        //when
        List<SpDetailRes> spDetails = spService.getRecommendationByPref(member.getId());

        //then
        // 검증 생략
        System.out.println("spDetails = " + spDetails.size());
        spDetails.forEach(System.out::println);
    }

    @Test
    void SP_수정_성공() {
        //given
        Sp sp = saveSp(bm);

        String newName = "new_name";
        MockMultipartFile newSpVid = new MockMultipartFile(
                "new_sp_vid", "new_sp_vid.mp4", "video/mp4", "test data 3".getBytes());
        MockMultipartFile newThumbnailImg = new MockMultipartFile(
                "new_thumbnail_img", "new_thumbnail_img.png", "image/png", "test data 4".getBytes());

        when(s3Service.uploadFile(newSpVid, S3UploadTarget.COMPANY_VIDEO))
                .thenReturn(newSpVid.getOriginalFilename());
        when(s3Service.uploadFile(newThumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL))
                .thenReturn(newThumbnailImg.getOriginalFilename());

        //when
        spService.updateSp(member.getId(), sp.getId(), newName, newSpVid, newThumbnailImg);

        //then
        List<Sp> all = spRepository.findAll();
        Sp updatedSp = all.get(0);
        assertThat(updatedSp.getBm()).isEqualTo(bm);
        assertThat(updatedSp.getSpKey()).isEqualTo(newSpVid.getOriginalFilename());
        assertThat(updatedSp.getThumbnailImgKey()).isEqualTo(newThumbnailImg.getOriginalFilename());
        assertThat(updatedSp.getName()).isEqualTo(newName);
    }

    @Test
    void SP_삭제_성공() {
        //given
        Sp sp = saveSp(bm);

        //when
        spService.deleteSp(member.getId(), sp.getId());

        //then
        List<Sp> all = spRepository.findAll();
        assertThat(all).isEmpty();
    }
}
