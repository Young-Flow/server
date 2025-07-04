package com.pitchain.service;

import com.pitchain.entity.Bm;
import com.pitchain.entity.BmScrap;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmScrapRepository;
import com.pitchain.repository.EntityFacade;
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
