package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.MySpHistory;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.MemberRepository;
import com.pitchain.repository.MySpHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MySpHistoryService {

    private final MySpHistoryRepository mySpHistoryRepository;
    private final MemberRepository memberRepository;
    private final BmRepository bmRepository;

    @Transactional
    public void saveMySpHistory(MemberDetails memberDetails, Long bmId, int viewTime) {
        Member member = memberRepository.findById(memberDetails.id())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Bm bm = bmRepository.findById(bmId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BM_NOT_FOUND));

        mySpHistoryRepository.findByMemberAndBm(member, bm)
                .ifPresentOrElse(
                        existingHistory -> existingHistory.updateViewTime(viewTime),
                        () -> mySpHistoryRepository.save(new MySpHistory(member, bm, viewTime))
                );
    }
}
