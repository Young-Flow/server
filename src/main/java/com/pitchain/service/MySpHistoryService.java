package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.MySpHistory;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.MemberRepository;
import com.pitchain.repository.MySpHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MySpHistoryService {

    private final MySpHistoryRepository mySpHistoryRepository;
    private final MemberRepository memberRepository;
    private final BmRepository bmRepository;

    @Transactional
    public void saveMySpHistory(Long memberId, Long bmId, int viewTime) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Bm bm = bmRepository.findById(bmId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.BM_NOT_FOUND));

        Optional<MySpHistory> existingMySpHistory = mySpHistoryRepository.findByMemberAndBm(member, bm);
        if (existingMySpHistory.isEmpty()) {
            mySpHistoryRepository.save(new MySpHistory(member, bm, viewTime));
        } else {
            MySpHistory mySpHistory = existingMySpHistory.get();
            mySpHistory.updateViewTime(viewTime);
        }
    }
}
