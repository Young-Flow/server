package com.pitchain.service;

import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.MyBmHistory;
import com.pitchain.repository.MyBmHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MyBmHistoryService {
    private final MyBmHistoryRepository myBmHistoryRepository;

    @Transactional
    public void saveMyBmHistory(Member member, Bm bm) {
        if (!myBmHistoryRepository.existsByMemberAndBm(member, bm))
            myBmHistoryRepository.save(MyBmHistory.of(member, bm));
    }
}
