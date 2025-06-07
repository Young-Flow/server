package com.pitchain.service;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.entity.Bm;
import com.pitchain.entity.BmScrap;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmScrapRepository;
import com.pitchain.util.EntitySaver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
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
