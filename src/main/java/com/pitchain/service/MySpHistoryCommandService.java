package com.pitchain.service;

import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.MySpHistory;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.MySpHistoryRepository;
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
