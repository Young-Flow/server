package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.res.InvestmentStatusRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Investment;
import com.pitchain.entity.Member;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.InvestmentRepository;
import com.pitchain.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class InvestmentServiceTest {

    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private InvestmentRepository investmentRepository;

    @Test
    void 투자_등록_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);
        long amount = 1000L;

        //when
        investmentService.addInvestment(bm.getId(), member.getId(), amount);

        //then
        List<Investment> investments = investmentRepository.findAll();
        Investment investment = investments.get(0);
        assertThat(investment.getMember()).isEqualTo(member);
        assertThat(investment.getBm()).isEqualTo(bm);
        assertThat(investment.getAmount()).isEqualTo(amount);
    }

    @Test
    void 투자_등록_실패() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);
        long amount = 1000L;

        Long invalidId = Long.MAX_VALUE;

        //when
        GeneralHandler error1 = Assertions.assertThrows(GeneralHandler.class, () -> investmentService.addInvestment(invalidId, member.getId(), amount));
        GeneralHandler error2 = Assertions.assertThrows(GeneralHandler.class, () -> investmentService.addInvestment(bm.getId(),invalidId, amount));

        //then
        assertThat(error1.getErrorStatus()).isEqualTo(ErrorStatus.BM_NOT_FOUND);
        assertThat(error2.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
    }

    @Test
    void BM_투자_정보_조회_성공() {
        //given
        Member member = saveMember();
        Long bmId = saveBm(member).getId();

        Member investorA = saveMember();
        long amountA = 1000L;

        Member investorB = saveMember();
        long amountB = 2000L;

        investmentService.addInvestment(bmId, investorA.getId(), amountA);
        investmentService.addInvestment(bmId, investorB.getId(), amountB);

        //when
        InvestmentStatusRes investmentStatus = investmentService.getInvestmentStatus(bmId);

        //then
        Bm bm  = bmRepository.findById(bmId).orElseThrow();
        assertThat(investmentStatus.deadline()).isEqualTo(bm.getDeadline());
        assertThat(investmentStatus.pricePerShare()).isEqualTo(bm.getPricePerShare());
        assertThat(investmentStatus.maxIssuedShare()).isEqualTo(bm.getMaxIssuedShare());
        assertThat(investmentStatus.valuationCap()).isEqualTo(bm.getValuationCap());
        assertThat(investmentStatus.maximumAmount()).isEqualTo(amountB);
        assertThat(investmentStatus.minimumAmount()).isEqualTo(amountA);
        assertThat(investmentStatus.investorNum()).isEqualTo(2);

        long raisedAmount = investmentStatus.raisedAmount();
        assertThat(raisedAmount).isEqualTo(amountA + amountB);
        Integer goalInvestment = investmentStatus.goalInvestment();
        assertThat(goalInvestment).isEqualTo(bm.getGoalInvestment());
        assertThat(investmentStatus.achievementRate()).isEqualTo(Math.min((int) ((raisedAmount / (double) goalInvestment) * 100), 100));
    }

    private Member saveMember() {
        return memberRepository.save(new Member("name", UUID.randomUUID().toString(), Country.USA, "profileImg"));
    }

    private Bm saveBm(Member member) {
        return bmRepository.save(new Bm(member, "bmName", MainCategory.FOOD, SubCategory.BEVERAGE_COFFEE, "bmCompany", "logoImg",
                "bmIntro", "bmDescription", "bmDescriptionImg", "companyAddress", 100000L,
                1000, 1000, LocalDate.now(), "longPitchUrl"));
    }
}