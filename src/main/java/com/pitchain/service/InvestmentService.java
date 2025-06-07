package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.dto.InvestmentStatusDto;
import com.pitchain.dto.res.InvestmentStatusRes;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Investment;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final EntityFacade entityFacade;

    public void addInvestment(Long bmId, MemberDetails memberDetails, long amount) {
        if (memberDetails.memberRole().equals(MemberRole.COMPANY))
            throw new GeneralException(ErrorStatus.COMPANY_FORBIDDEN);

        Member member = entityFacade.getMember(memberDetails.id());
        Bm bm = entityFacade.getBm(bmId);
        Investment investment = Investment.builder()
                .member(member)
                .bm(bm)
                .amount(amount)
                .build();

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
