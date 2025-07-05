package com.pitchain.bmscrap.application;

import com.pitchain.bm.domain.Bm;
import com.pitchain.bmscrap.domain.BmScrap;
import com.pitchain.member.domain.Member;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.bmscrap.infrastucture.BmScrapRepository;
import com.pitchain.common.application.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BmScrapCommandService {
    private final EntityFacade entityFacade;
    private final BmScrapRepository bmScrapRepository;

    @Transactional
    public void toggleScrapBm(Long bmId, MemberDetails memberDetails) {
        Member member = entityFacade.getMember(memberDetails.id());
        Bm bm = entityFacade.getBm(bmId);

        if (isScraped(member, bm)) {
            cancelScrap(member, bm);
        } else {
            addScrap(member, bm);
        }
    }

    private boolean isScraped(Member member, Bm bm) {
        return bmScrapRepository.existsByMemberAndBm(member, bm);
    }

    private void addScrap(Member member, Bm bm) {
        BmScrap bmScrap = new BmScrap(member, bm);
        bmScrapRepository.save(bmScrap);
    }

    private void cancelScrap(Member member, Bm bm) {
        bmScrapRepository.deleteByMemberAndBm(member, bm);
    }

}
