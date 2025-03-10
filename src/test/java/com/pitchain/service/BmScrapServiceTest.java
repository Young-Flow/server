package com.pitchain.service;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.entity.Bm;
import com.pitchain.entity.BmScrap;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
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
        Company company = saveCompany();
        Bm bm = saveBm(company);
        Member member = saveIndividual();

        //when
        bmScrapService.toggleScrapBm(bm.getId(), createIndividualMemberDetails(member));

        //then
        boolean isScraped = bmScrapRepository.existsByMemberAndBm(member, bm);
        assertThat(isScraped).isTrue();
    }

    @Test
    void BM_스크랩_취소_성공() {
        //given
        Company company = saveCompany();
        Bm bm = saveBm(company);
        Member member = saveIndividual();

        BmScrap bmScrap = new BmScrap(member, bm);
        bmScrapRepository.save(bmScrap);

        //when
        bmScrapService.toggleScrapBm(bm.getId(), createIndividualMemberDetails(member));

        //then
        boolean isScraped = bmScrapRepository.existsByMemberAndBm(member, bm);
        assertThat(isScraped).isFalse();
    }

    private Member saveIndividual() {
        return memberRepository.save(Member.fromIndividual("email", "name"));
    }

    private MemberDetails createIndividualMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }

    private Company saveCompany() {
        Member member = memberRepository.save(Member.fromCompany("email"));
        return companyRepository.save(new Company(member, "encodedPassword"));
    }

    private Bm saveBm(Company company) {
        Bm bm = new Bm(
                company, "bmName", MainCategory.FOOD,
                "bmIntro", "bmDescription", "bmDescriptionImg", "bmAddress",
                100000L, 1000L, 1000,
                LocalDate.now(), "longPitchUrl"
        );

        return bmRepository.save(bm);
    }

}
