package com.pitchain.service;

import com.pitchain.common.constant.MemberRole;
import com.pitchain.bm.domain.Bm;
import com.pitchain.company.domain.Company;
import com.pitchain.member.domain.Member;
import com.pitchain.mysphistory.domain.MySpHistory;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.mysphistory.application.MySpHistoryService;
import com.pitchain.mysphistory.infrastucture.MySpHistoryRepository;
import com.pitchain.util.EntitySaver;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MySpHistoryServiceTest {

    @Autowired
    private MySpHistoryService mySpHistoryService;
    @Autowired
    private MySpHistoryRepository mySpHistoryRepository;
    @Autowired
    private EntitySaver entitySaver;

    @Test
    void SP_시청시간_최초_저장_성공() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);

        Member individualMember = entitySaver.saveIndividualMember();

        MemberDetails individualMemberDetails = createIndividualMemberDetails(individualMember);
        int viewTime = 10000;

        //when
        mySpHistoryService.saveMySpHistory(individualMemberDetails, bm.getId(), viewTime);

        //then
        MySpHistory mySpHistory = mySpHistoryRepository.findByMemberAndBm(individualMember, bm).orElseThrow();
        Assertions.assertThat(mySpHistory.getBm()).isEqualTo(bm);
        Assertions.assertThat(mySpHistory.getMember()).isEqualTo(individualMember);
        Assertions.assertThat(mySpHistory.getViewTime()).isEqualTo(viewTime);
    }

    @Test
    void SP_업데이트_성공() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);

        Member individualMember = entitySaver.saveIndividualMember();

        MemberDetails individualMemberDetails = createIndividualMemberDetails(individualMember);
        int viewTime = 10000;

        mySpHistoryRepository.save(new MySpHistory(individualMember, bm, viewTime));

        //when
        int updatedViewTime = 20000;
        mySpHistoryService.saveMySpHistory(individualMemberDetails, bm.getId(), updatedViewTime);

        //then
        MySpHistory mySpHistory = mySpHistoryRepository.findByMemberAndBm(individualMember, bm).orElseThrow();
        Assertions.assertThat(mySpHistory.getBm()).isEqualTo(bm);
        Assertions.assertThat(mySpHistory.getMember()).isEqualTo(individualMember);
        Assertions.assertThat(mySpHistory.getViewTime()).isEqualTo(updatedViewTime);

    }

    private MemberDetails createIndividualMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }


}
