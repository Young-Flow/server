package com.pitchain.service;

import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.MySpHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MySpHistoryService {
    private final MySpHistoryCommandService mySpHistoryCommandService;

    @Transactional
    public void saveMySpHistory(MemberDetails memberDetails, Long bmId, int viewTime) {
        mySpHistoryCommandService.saveMySpHistory(memberDetails, bmId, viewTime);
    }
}
