package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.res.InvestmentStatusRes;
import com.pitchain.entity.*;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class InvestmentServiceTest {

    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private InvestmentRepository investmentRepository;
    @Autowired
    private IndividualRepository individualRepository;

    @Test
    void 투자_등록_성공() {
        //given
        Member individual = saveIndividual();
        MemberDetails individualMemberDetails = createIndividualMemberDetails(individual);

        Company company = saveCompany();
        Bm bm = saveBm(company);
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
        Member individual = saveIndividual();
        MemberDetails individualMemberDetails = createIndividualMemberDetails(individual);

        Company company = saveCompany();
        Bm bm = saveBm(company);
        long amount = 1000L;

        Long invalidId = Long.MAX_VALUE;
        MemberDetails invalidIndividualMemberDetails = new MemberDetails(Long.MAX_VALUE, MemberRole.INDIVIDUAL);

        //when
        GeneralHandler error1 = Assertions.assertThrows(GeneralHandler.class, () -> investmentService.addInvestment(invalidId, individualMemberDetails, amount));
        GeneralHandler error2 = Assertions.assertThrows(GeneralHandler.class, () -> investmentService.addInvestment(bm.getId(), invalidIndividualMemberDetails, amount));

        //then
        assertThat(error1.getErrorStatus()).isEqualTo(ErrorStatus.BM_NOT_FOUND);
        assertThat(error2.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
    }

    @Test
    void BM_투자_정보_조회_성공() {
        //given
        Company company = saveCompany();
        Long bmId = saveBm(company).getId();

        Member individualA = saveIndividual();
        MemberDetails individualMemberDetailsA = createIndividualMemberDetails(individualA);
        long amountA = 1000L;

        Member individualB = saveIndividual();
        MemberDetails individualMemberDetailsB = createIndividualMemberDetails(individualB);
        long amountB = 2000L;

        investmentService.addInvestment(bmId, individualMemberDetailsA, amountA);
        investmentService.addInvestment(bmId, individualMemberDetailsB, amountB);

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

    private Member saveIndividual() {
        return memberRepository.save(Member.createIndividualMember("email", "name"));
    }

    private MemberDetails createIndividualMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }

    private Company saveCompany() {
        Member member = memberRepository.save(Member.createCompanyMember("email"));
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
