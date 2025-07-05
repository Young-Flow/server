package com.pitchain.mysphistory.presentation;

import com.pitchain.mysphistory.presentation.req.MySpHistoryCreateReq;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.mysphistory.application.MySpHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MySpHistoryController {

    private final MySpHistoryService mySpHistoryService;

    @Operation(summary = "사용자의 SP 시청 시간을 최초 저장 및 업데이트")
    @PostMapping("/histories")
    public void saveMySpHistory(@AuthenticationPrincipal MemberDetails memberDetails,
                                @RequestBody @Valid MySpHistoryCreateReq req) {
        mySpHistoryService.saveMySpHistory(memberDetails, req.getBmId(), req.getViewTime());
    }
}
