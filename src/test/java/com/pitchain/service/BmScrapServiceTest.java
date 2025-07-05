package com.pitchain.service;

import com.pitchain.bmscrap.application.BmScrapService;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.bm.domain.Bm;
import com.pitchain.bmscrap.domain.BmScrap;
import com.pitchain.company.domain.Company;
import com.pitchain.member.domain.Member;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.bmscrap.infrastucture.BmScrapRepository;
import com.pitchain.util.EntitySaver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class BmScrapServiceTest {

    @Autowired
    private BmScrapService bmScrapService;
    @Autowired
    private BmScrapRepository bmScrapRepository;
    @Autowired
    private EntitySaver entitySaver;

    @Test
    void BM_스크랩_등록_성공() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);
        Member individual = entitySaver.saveIndividualMember();

        //when
        bmScrapService.toggleScrapBm(bm.getId(), createIndividualMemberDetails(individual));

        //then
        boolean isScraped = bmScrapRepository.existsByMemberAndBm(individual, bm);
        assertThat(isScraped).isTrue();
    }

    @Test
    void BM_스크랩_취소_성공() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);
        Member individual = entitySaver.saveIndividualMember();

        BmScrap bmScrap = new BmScrap(individual, bm);
        bmScrapRepository.save(bmScrap);

        //when
        bmScrapService.toggleScrapBm(bm.getId(), createIndividualMemberDetails(individual));

        //then
        boolean isScraped = bmScrapRepository.existsByMemberAndBm(individual, bm);
        assertThat(isScraped).isFalse();
    }


    private MemberDetails createIndividualMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }
}
