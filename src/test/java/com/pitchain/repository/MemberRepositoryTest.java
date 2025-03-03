package com.pitchain.repository;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.dto.PreferenceInfoDto;
import com.pitchain.dto.res.MemberPreferenceInfoRes;
import com.pitchain.dto.res.PreferenceInfoRes;
import com.pitchain.entity.*;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private SpRepository spRepository;
    @Autowired
    private InvestmentRepository investmentRepository;
    @Autowired
    private MyBmHistoryRepository myBmHistoryRepository;
    @Autowired
    private MySpHistoryRepository mySpHistoryRepository;

    private static final String COMPANY_EMAIL = "companyEmail";
    private static final String COMPANY_PASSWORD = "companyPassword";
    private static final String COMPANY_NAME = "companyName";
    private static final String COMPANY_ADDRESS = "companyAddress";
    private static final String LOGO_IMG_KEY = "bm_logo_img_key";
    private static final String SP_NAME = "sp_name";
    private static final String SP_KEY = "sp_vid.m3u8";
    private static final MockMultipartFile THUMBNAIL_IMG = new MockMultipartFile(
            "thumbnail_img", "thumbnail_img.png", "image/png", "test data 2".getBytes());

    private Member saveMember() {
        return memberRepository.save(new Member(Country.ROK, Strings.EMPTY));
    }

    private Company saveCompany() {
        return companyRepository.save(new Company(COMPANY_EMAIL, COMPANY_PASSWORD, false, COMPANY_NAME, COMPANY_ADDRESS, LOGO_IMG_KEY));
    }

    private Bm saveBm(Company company) {
        return bmRepository.save(new Bm(company, "bm_name", MainCategory.FOOD, "bm_intro", "bm_description", "bm_desc_img_key",
                "bm_address", 100000L, 1000L, 1000, LocalDate.now(), "bm_long_pitch_url"));
    }

    private Bm saveBmWithMainCategory(Company company, MainCategory mainCategory) {
        return bmRepository.save(new Bm(company, "bm_name", mainCategory, "bm_intro", "bm_description", "bm_desc_img_key",
                "bm_address", 100000L, 1000L, 1000, LocalDate.now(), "bm_long_pitch_url"));
    }

    private Sp saveSp(Bm bm) {
        return spRepository.save(new Sp(bm, SP_KEY, THUMBNAIL_IMG.getOriginalFilename(), SP_NAME));
    }

    @Test
    void 유저_선호도_정보_조회() {
        // given
        Member member_1 = saveMember();
        Member member_2 = saveMember();
        Member member_3 = saveMember();
        Member member_4 = saveMember();

        Company company_1 = saveCompany();
        Company company_2 = saveCompany();
        Company company_3 = saveCompany();
        Company company_4 = saveCompany();

        Bm bm_1 = saveBmWithMainCategory(company_1, MainCategory.FOOD);

        Bm bm_2 = saveBmWithMainCategory(company_2, MainCategory.FOOD);
        investmentRepository.save(new Investment(member_2, bm_2, 2000L));

        Bm bm_3 = saveBmWithMainCategory(company_3, MainCategory.FOOD);
        investmentRepository.save(new Investment(member_3, bm_3, 3000L));
        mySpHistoryRepository.save(new MySpHistory(member_3, bm_3, 98765));

        Bm bm_4 = saveBmWithMainCategory( company_4, MainCategory.FOOD);
        investmentRepository.save(new Investment(member_4, bm_4, 4000L));
        mySpHistoryRepository.save(new MySpHistory(member_4, bm_4, 123456));
        myBmHistoryRepository.save(new MyBmHistory(member_4, bm_4));

        // when
        List<PreferenceInfoDto> preferenceInfoDtos = memberRepository.getMemberPreferenceInfos();

        // then
        Map<Long, List<PreferenceInfoDto>> preferenceInfoGroupedByMemberId = preferenceInfoDtos.stream().collect(Collectors.groupingBy(PreferenceInfoDto::memberId));

        List<MemberPreferenceInfoRes> memberPreferenceInfoResList = preferenceInfoGroupedByMemberId.entrySet().stream()
                .map(entry -> {
                    Long memberId = entry.getKey();
                    List<PreferenceInfoRes> preferenceInfoResList = entry.getValue().stream().map(PreferenceInfoRes::createRes).toList();
                    return new MemberPreferenceInfoRes(memberId, preferenceInfoResList);
                })
                .toList();

        System.out.println("memberPreferenceInfoResList = " + memberPreferenceInfoResList);

        assertThat(preferenceInfoDtos.size()).isEqualTo(16);
    }
}
