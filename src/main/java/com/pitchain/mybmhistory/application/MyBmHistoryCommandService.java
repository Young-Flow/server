package com.pitchain.mybmhistory.application;

import com.pitchain.bm.domain.Bm;
import com.pitchain.member.domain.Member;
import com.pitchain.mybmhistory.domain.MyBmHistory;
import com.pitchain.mybmhistory.infrastructure.MyBmHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyBmHistoryCommandService {
    private final MyBmHistoryRepository myBmHistoryRepository;

    @Transactional
    public void saveMyBmHistory(Member member, Bm bm) {
        if (!myBmHistoryRepository.existsByMemberAndBm(member, bm))
            myBmHistoryRepository.save(MyBmHistory.of(member, bm));
    }
}
