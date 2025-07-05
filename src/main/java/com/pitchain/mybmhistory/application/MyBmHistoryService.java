package com.pitchain.mybmhistory.application;

import com.pitchain.bm.domain.Bm;
import com.pitchain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MyBmHistoryService {
    private final MyBmHistoryCommandService myBmHistoryCommandService;

    @Transactional
    public void saveMyBmHistory(Member member, Bm bm) {
        myBmHistoryCommandService.saveMyBmHistory(member, bm);
    }
}
