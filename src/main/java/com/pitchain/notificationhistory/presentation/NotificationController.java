package com.pitchain.notificationhistory.presentation;

import com.pitchain.common.security.MemberDetails;
import com.pitchain.notificationhistory.application.NotificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class NotificationController {
    private final NotificationHistoryService notificationHistoryService;

    @GetMapping(value = "/notifications", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal MemberDetails memberDetails) {
        return notificationHistoryService.subscribe(memberDetails.id());
    }
}
