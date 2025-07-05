package com.pitchain.mysphistory.application;

import com.pitchain.common.security.MemberDetails;
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
