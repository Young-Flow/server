package com.pitchain.mysphistory.application;

import com.pitchain.bm.domain.Bm;
import com.pitchain.member.domain.Member;
import com.pitchain.mysphistory.domain.MySpHistory;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.common.application.EntityFacade;
import com.pitchain.mysphistory.infrastucture.MySpHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MySpHistoryCommandService {
    private final EntityFacade entityFacade;
    private final MySpHistoryRepository mySpHistoryRepository;

    @Transactional
    public void saveMySpHistory(MemberDetails memberDetails, Long bmId, int viewTime) {
        Member member = entityFacade.getMember(memberDetails.id());
        Bm bm = entityFacade.getBm(bmId);

        mySpHistoryRepository.findByMemberAndBm(member, bm)
                .ifPresentOrElse(
                        existingHistory -> existingHistory.updateViewTime(viewTime),
                        () -> mySpHistoryRepository.save(new MySpHistory(member, bm, viewTime))
                );
    }
}
