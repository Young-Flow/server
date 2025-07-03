package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.dto.res.InvestmentStatusRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Company;
import com.pitchain.entity.Investment;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.InvestmentRepository;
import com.pitchain.util.EntitySaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class InvestmentServiceTest {

    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private InvestmentRepository investmentRepository;
    @Autowired
    private EntitySaver entitySaver;

    @Test
    void 투자_등록_성공() {
        //given
        Member individual = entitySaver.saveIndividualMember();
        MemberDetails individualMemberDetails = createIndividualMemberDetails(individual);

        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);
        long amount = 1000L;

        //when
        investmentService.addInvestment(bm.getId(), individualMemberDetails, amount);

        //then
        List<Investment> investments = investmentRepository.findAll();
        Investment investment = investments.get(0);
        assertThat(investment.getMember()).isEqualTo(individual);
        assertThat(investment.getBm()).isEqualTo(bm);
        assertThat(investment.getAmount()).isEqualTo(amount);
    }

    @Test
    void 투자_등록_실패() {
        //given
        Member individual = entitySaver.saveIndividualMember();
        MemberDetails individualMemberDetails = createIndividualMemberDetails(individual);

        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);
        long amount = 1000L;

        Long invalidId = Long.MAX_VALUE;
        MemberDetails invalidIndividualMemberDetails = new MemberDetails(Long.MAX_VALUE, MemberRole.INDIVIDUAL);

        //when
        GeneralException error1 = Assertions.assertThrows(GeneralException.class, () -> investmentService.addInvestment(invalidId, individualMemberDetails, amount));
        GeneralException error2 = Assertions.assertThrows(GeneralException.class, () -> investmentService.addInvestment(bm.getId(), invalidIndividualMemberDetails, amount));

        //then
        assertThat(error1.getErrorStatus()).isEqualTo(ErrorStatus.BM_NOT_FOUND);
        assertThat(error2.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
    }

    @Test
    void BM_투자_정보_조회_성공() {
        //given
        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Long bmId = entitySaver.saveBm(company).getId();

        Member individual_1 = entitySaver.saveIndividualMember();
        MemberDetails individualMemberDetails_1 = createIndividualMemberDetails(individual_1);
        long amountA = 1000L;

        Member individual_2 = entitySaver.saveIndividualMember();
        MemberDetails individualMemberDetails_2 = createIndividualMemberDetails(individual_2);
        long amountB = 2000L;

        investmentService.addInvestment(bmId, individualMemberDetails_1, amountA);
        investmentService.addInvestment(bmId, individualMemberDetails_2, amountB);

        //when
        InvestmentStatusRes investmentStatus = investmentService.getInvestmentStatus(bmId);

        //then
        Bm bm = bmRepository.findById(bmId).orElseThrow();
        assertThat(investmentStatus.deadline()).isEqualTo(bm.getDeadline());
        assertThat(investmentStatus.pricePerShare()).isEqualTo(bm.getPricePerShare());
        assertThat(investmentStatus.maxIssuedShare()).isEqualTo(bm.getMaxIssuedShare());
        assertThat(investmentStatus.valuationCap()).isEqualTo(bm.getValuationCap());
        assertThat(investmentStatus.maximumAmount()).isEqualTo(amountB);
        assertThat(investmentStatus.minimumAmount()).isEqualTo(amountA);
        assertThat(investmentStatus.investorNum()).isEqualTo(2);

        long raisedAmount = investmentStatus.raisedAmount();
        assertThat(raisedAmount).isEqualTo(amountA + amountB);
        Long goalInvestment = investmentStatus.goalInvestment();
        assertThat(goalInvestment).isEqualTo(bm.getGoalInvestment());
        assertThat(investmentStatus.achievementRate()).isEqualTo(Math.min((int) ((raisedAmount / (double) goalInvestment) * 100), 100));
    }

    private MemberDetails createIndividualMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }

}
