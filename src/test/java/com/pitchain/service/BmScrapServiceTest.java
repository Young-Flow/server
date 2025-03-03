package com.pitchain.service;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.entity.Bm;
import com.pitchain.entity.BmScrap;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.CompanyRepository;
import com.pitchain.repository.MemberRepository;
import com.pitchain.repository.BmScrapRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class BmScrapServiceTest {

    @Autowired
    private BmScrapService bmScrapService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private BmScrapRepository bmScrapRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void BM_스크랩_등록_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm();

        //when
        bmScrapService.toggleScrapBm(bm.getId(), member.getId());

        //then
        boolean isScraped = bmScrapRepository.existsByMemberAndBm(member, bm);
        assertThat(isScraped).isTrue();
    }

    @Test
    void BM_스크랩_취소_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm();

        BmScrap bmScrap = new BmScrap(member, bm);
        bmScrapRepository.save(bmScrap);

        //when
        bmScrapService.toggleScrapBm(bm.getId(), member.getId());

        //then
        boolean isScraped = bmScrapRepository.existsByMemberAndBm(member, bm);
        assertThat(isScraped).isFalse();
    }

    private Member saveMember() {
        return memberRepository.save(new Member(Country.USA, "profileImg"));
    }

    private Bm saveBm() {
        Company company = companyRepository.save(new Company(
                "companyEmail", "companyPassword", false,
                "companyName", "companyAddress", "bm_logo_img_key"
        ));

        Bm bm = new Bm(
                company, "bmName", MainCategory.FOOD,
                "bmIntro", "bmDescription", "bmDescriptionImg", "bmAddress",
                100000L, 1000L, 1000,
                LocalDate.now(), "longPitchUrl"
        );

        return bmRepository.save(bm);
    }

}
