package com.pitchain.service;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.entity.MySpHistory;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.CompanyRepository;
import com.pitchain.repository.MemberRepository;
import com.pitchain.repository.MySpHistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class MySpHistoryServiceTest {

    @Autowired
    private MySpHistoryService mySpHistoryService;
    @Autowired
    private MySpHistoryRepository mySpHistoryRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void SP_시청시간_최초_저장_성공() {
        //given
        Company company = saveCompany();
        Bm bm = saveBm(company);

        Member member = saveMember();
        int viewTime = 10000;

        //when
        mySpHistoryService.saveMySpHistory(member.getId(), bm.getId(), viewTime);

        //then
        MySpHistory mySpHistory = mySpHistoryRepository.findByMemberAndBm(member, bm).orElseThrow();
        Assertions.assertThat(mySpHistory.getBm()).isEqualTo(bm);
        Assertions.assertThat(mySpHistory.getMember()).isEqualTo(member);
        Assertions.assertThat(mySpHistory.getViewTime()).isEqualTo(viewTime);
    }

    @Test
    void SP_업데이트_성공() {
        //given
        Company company = saveCompany();
        Bm bm = saveBm(company);

        Member member = saveMember();
        int viewTime = 10000;

        mySpHistoryRepository.save(new MySpHistory(member, bm, viewTime));

        //when
        int updatedViewTime = 20000;
        mySpHistoryService.saveMySpHistory(member.getId(), bm.getId(), updatedViewTime);

        //then
        MySpHistory mySpHistory = mySpHistoryRepository.findByMemberAndBm(member, bm).orElseThrow();
        Assertions.assertThat(mySpHistory.getBm()).isEqualTo(bm);
        Assertions.assertThat(mySpHistory.getMember()).isEqualTo(member);
        Assertions.assertThat(mySpHistory.getViewTime()).isEqualTo(updatedViewTime);

    }

    private Member saveMember() {
        return memberRepository.save(new Member(Country.USA, "profileImg.jpg"));
    }

    private Bm saveBm(Company company) {
        return bmRepository.save(new Bm(company, "bmName", MainCategory.FOOD, "bmIntro", "bmDescription",
                "bmDescriptionImg", "companyAddress", 100000L, 1000L, 1000, LocalDate.now(), "longPitchUrl"));
    }

    private Company saveCompany() {
        return companyRepository.save(new Company("companyEmail", "companyPassword",
                true, "companyName", "companyAddress", "bm_logo_img_key"
        ));
    }
}
