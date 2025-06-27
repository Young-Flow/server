package com.pitchain.service;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.entity.InfinityScrollRes;
import com.pitchain.dto.req.SpCreateReq;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.entity.*;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.BmSubCategoryRepository;
import com.pitchain.repository.SpLikeRepository;
import com.pitchain.repository.SpRepository;
import com.pitchain.util.EntitySaver;
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
    private BmRepository bmRepository;
    @Autowired
    private SpLikeRepository spLikeRepository;
    @Autowired
    private BmSubCategoryRepository bmSubCategoryRepository;
    @Autowired
    private EntitySaver entitySaver;

    private MemberDetails createIndividualMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }

    private MemberDetails createCompanyMemberDetails(Company company) {
        return new MemberDetails(company.getMember().getId(), MemberRole.COMPANY);
    }

    private Member individualMember;
    private MemberDetails individualMemberDetails;
    private Bm bm;
    private Company company;
    private MemberDetails companyMemberDetails;

    @BeforeEach
    void setUp() {
        individualMember = entitySaver.saveIndividualMember();
        Member companyMember = entitySaver.saveCompanyMember();
        company = entitySaver.saveCompany(companyMember);
        bm = entitySaver.saveBm(company);
        companyMemberDetails = createCompanyMemberDetails(company);
        individualMemberDetails = createIndividualMemberDetails(individualMember);
    }

    private static final String SP_NAME = "sp_name";
    private static final MockMultipartFile THUMBNAIL_IMG = new MockMultipartFile(
            "thumbnail_img", "thumbnail_img.png", "image/png", "test data 2".getBytes());
    private static final List<SubCategory> SUB_CATEGORIES = List.of(SubCategory.BEVERAGE_COFFEE, SubCategory.ALCOHOL);

    @Test
    void SP_생성_성공() {
        //given
        SpCreateReq spCreateReq = new SpCreateReq(SP_NAME);

        when(s3Service.uploadFile(THUMBNAIL_IMG, S3UploadTarget.COMPANY_THUMBNAIL))
                .thenReturn(THUMBNAIL_IMG.getOriginalFilename());

        //when
        spService.createSp(companyMemberDetails, bm.getId(), spCreateReq, THUMBNAIL_IMG);

        //then
        List<Sp> all = spRepository.findAll();
        Sp sp = all.get(0);
        assertThat(sp.getBm()).isEqualTo(bm);
        assertThat(sp.getThumbnailImgKey()).isEqualTo(THUMBNAIL_IMG.getOriginalFilename());
        assertThat(sp.getName()).isEqualTo(SP_NAME);
    }

    @Test
    void SP_조회_성공_좋아요_없음() {
        //given
        Sp sp = entitySaver.saveSp(bm);

        for (SubCategory subCategory : SUB_CATEGORIES) {
            BmSubCategory bmSubCategory = BmSubCategory.create(bm, subCategory);
            bmSubCategoryRepository.save(bmSubCategory);
        }

        //when
        SpDetailRes spDetail = spService.getSpDetail(individualMemberDetails, bm.getId(), sp.getId());

        //then
        assertThat(spDetail.bmId()).isEqualTo(sp.getBm().getId());
        assertThat(spDetail.spURL()).isEqualTo(sp.getSpKey());
        assertThat(spDetail.thumbnailImgURL()).isEqualTo(sp.getThumbnailImgKey());
        assertThat(spDetail.views()).isEqualTo(sp.getViews());
        assertThat(spDetail.name()).isEqualTo(sp.getName());
        assertThat(spDetail.mainCategory()).isEqualTo(sp.getBm().getMainCategory().getKoreanName());
        assertThat(spDetail.subCategories()).isEqualTo(SUB_CATEGORIES.stream().map(SubCategory::getKoreanName).toList());
        assertThat(spDetail.isLiked()).isEqualTo(false);
        assertThat(spDetail.likeCnt()).isEqualTo(0L);
    }

    @Test
    void SP_조회_성공_좋아요_존재() {
        //given
        Member newIndividual = entitySaver.saveIndividualMember();
        Sp sp = entitySaver.saveSp(bm);

        spLikeRepository.save(new SpLike(individualMember, sp));
        spLikeRepository.save(new SpLike(newIndividual, sp));

        for (SubCategory subCategory : SUB_CATEGORIES) {
            BmSubCategory bmSubCategory = BmSubCategory.create(bm, subCategory);
            bmSubCategoryRepository.save(bmSubCategory);
        }

        //when
        SpDetailRes spDetail = spService.getSpDetail(individualMemberDetails, bm.getId(), sp.getId());

        //then
        assertThat(spDetail.bmId()).isEqualTo(sp.getBm().getId());
        assertThat(spDetail.spURL()).isEqualTo(sp.getSpKey());
        assertThat(spDetail.thumbnailImgURL()).isEqualTo(sp.getThumbnailImgKey());
        assertThat(spDetail.views()).isEqualTo(sp.getViews());
        assertThat(spDetail.name()).isEqualTo(sp.getName());
        assertThat(spDetail.mainCategory()).isEqualTo(sp.getBm().getMainCategory().getKoreanName());
        assertThat(spDetail.subCategories()).isEqualTo(SUB_CATEGORIES.stream().map(SubCategory::getKoreanName).toList());
        assertThat(spDetail.isLiked()).isEqualTo(true);
        assertThat(spDetail.likeCnt()).isEqualTo(2L);
    }

    @Test
    void SP_전체_조회() {
        //given
        Sp sp_1 = entitySaver.saveSp(bm);

        Member companyMember = entitySaver.saveCompanyMember();
        Company newCompany = entitySaver.saveCompany(companyMember);
        Bm newBm = entitySaver.saveBm(newCompany);
        Sp sp_2 = entitySaver.saveSp(newBm);

        //when
        List<SpDetailRes> spDetails = spService.getSpDetails(individualMemberDetails);

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
        Member individual_1 = entitySaver.saveIndividualMember();
        Member individual_2 = entitySaver.saveIndividualMember();
        Member individual_3 = entitySaver.saveIndividualMember();

        MemberDetails individualMemberDetails_1 = createIndividualMemberDetails(individual_1);

        Company company1 = entitySaver.saveCompany(entitySaver.saveCompanyMember());
        Company company2 = entitySaver.saveCompany(entitySaver.saveCompanyMember());
        Company company3 = entitySaver.saveCompany(entitySaver.saveCompanyMember());

        Bm bm1 = saveBmWithMainCategory(company1, MainCategory.TECH_DIGITAL);
        Bm bm2 = saveBmWithMainCategory(company2, MainCategory.TECH_DIGITAL);
        Bm bm3 = saveBmWithMainCategory(company3, MainCategory.FOOD);

        Sp sp1 = entitySaver.saveSp(bm1);
        Sp sp2 = entitySaver.saveSp(bm2);
        Sp sp3 = entitySaver.saveSp(bm3);

        spLikeRepository.save(new SpLike(individual_1, sp1));
        spLikeRepository.save(new SpLike(individual_2, sp1));

        //when
        InfinityScrollRes<SpDetailRes> spDetailRes_1 = spService.getSpDetailsFilteredCategory(
                individualMemberDetails_1, MainCategory.TECH_DIGITAL.getKoreanName(), null, 1);
        InfinityScrollRes<SpDetailRes> spDetailRes_2 = spService.getSpDetailsFilteredCategory(
                individualMemberDetails_1, MainCategory.TECH_DIGITAL.getKoreanName(), spDetailRes_1.getLastElementId(), 1);

        // then
        List<SpDetailRes> content_1 = spDetailRes_1.getContent();
        SpDetailRes spDetailRes = content_1.get(0); // sp2 조회 결과
        assertThat(spDetailRes.bmId()).isEqualTo(sp2.getBm().getId());
        assertThat(content_1.get(0).mainCategory()).isEqualTo(bm1.getMainCategory().getKoreanName());
        assertThat(content_1.get(0).name()).isEqualTo(sp1.getName());
        assertThat(content_1.get(0).views()).isEqualTo(sp1.getViews());
        assertThat(content_1.get(0).isLiked()).isFalse();
        assertThat(content_1.get(0).likeCnt()).isEqualTo(0);
        assertThat(spDetailRes_1.hasNext()).isTrue();
        assertThat(spDetailRes_1.getLastElementId()).isEqualTo(sp2.getId());

        List<SpDetailRes> content_2 = spDetailRes_2.getContent();
        spDetailRes = content_2.get(0); // sp1 조회 결과
        assertThat(spDetailRes.bmId()).isEqualTo(sp1.getBm().getId());
        assertThat(spDetailRes.mainCategory()).isEqualTo(bm1.getMainCategory().getKoreanName());
        assertThat(spDetailRes.name()).isEqualTo(sp1.getName());
        assertThat(spDetailRes.views()).isEqualTo(sp1.getViews());
        assertThat(spDetailRes.isLiked()).isTrue();
        assertThat(spDetailRes.likeCnt()).isEqualTo(2);
        assertThat(spDetailRes_2.hasNext()).isFalse();
        assertThat(spDetailRes_2.getLastElementId()).isEqualTo(sp1.getId());
    }

    private Bm saveBmWithMainCategory(Company company, MainCategory mainCategory) {
        return bmRepository.save(Bm.create(company, "bm_name", mainCategory, "bm_intro", "bm_description",
                "bm_desc_img_key", "bm_address", 100000L, 1000L, 1000, LocalDate.now(), "bm_long_pitch_url"));
    }

    @Test
    void SP_수정_성공() {
        //given
        Sp sp = entitySaver.saveSp(bm);

        String newName = "new_name";
        MockMultipartFile newSpVid = new MockMultipartFile(
                "new_sp_vid", "new_sp_vid.mp4", "video/mp4", "test data 3".getBytes());
        MockMultipartFile newThumbnailImg = new MockMultipartFile(
                "new_thumbnail_img", "new_thumbnail_img.png", "image/png", "test data 4".getBytes());

        when(s3Service.uploadFile(newThumbnailImg, S3UploadTarget.COMPANY_THUMBNAIL))
                .thenReturn(newThumbnailImg.getOriginalFilename());

        //when
        spService.updateSp(companyMemberDetails, bm.getId(), sp.getId(), newName, newThumbnailImg);

        //then
        List<Sp> all = spRepository.findAll();
        Sp updatedSp = all.get(0);
        assertThat(updatedSp.getBm()).isEqualTo(bm);
        assertThat(updatedSp.getThumbnailImgKey()).isEqualTo(newThumbnailImg.getOriginalFilename());
        assertThat(updatedSp.getName()).isEqualTo(newName);
    }

    @Test
    void SP_삭제_성공() {
        //given
        Sp sp = entitySaver.saveSp(bm);

        //when
        spService.deleteSp(companyMemberDetails, bm.getId(), sp.getId());

        //then
        List<Sp> all = spRepository.findAll();
        assertThat(all).isEmpty();
    }
}
