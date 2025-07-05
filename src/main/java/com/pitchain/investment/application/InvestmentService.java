package com.pitchain.investment.application;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.investment.infrastucture.dto.InvestmentStatusDto;
import com.pitchain.investment.application.res.InvestmentStatusRes;
import com.pitchain.bm.domain.Bm;
import com.pitchain.investment.domain.Investment;
import com.pitchain.member.domain.Member;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.common.application.EntityFacade;
import com.pitchain.investment.infrastucture.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final EntityFacade entityFacade;

    @Transactional
    public void addInvestment(Long bmId, MemberDetails memberDetails, long amount) {
        if (memberDetails.memberRole().equals(MemberRole.COMPANY))
            throw new GeneralException(ErrorStatus.COMPANY_FORBIDDEN);

        Member member = entityFacade.getMember(memberDetails.id());
        Bm bm = entityFacade.getBm(bmId);
        Investment investment = Investment.of(member, bm, amount);

        investmentRepository.save(investment);
    }

    @Transactional(readOnly = true)
    public InvestmentStatusRes getInvestmentStatus(Long bmId) {
        Bm bm = entityFacade.getBm(bmId);

        InvestmentStatusDto investmentStatusDto = investmentRepository.findInvestmentStatusByBm(bm);
        int achievementRate = getAchievementRate(bm.getGoalInvestment(), investmentStatusDto.getRaisedAmount());

        return InvestmentStatusRes.createRes(bm, investmentStatusDto, achievementRate);
    }

    private int getAchievementRate(long goalInvestment, long raisedAmount) {
        int rate = (int) ((raisedAmount / (double) goalInvestment) * 100);
        return Math.min(rate, 100);
    }

}
