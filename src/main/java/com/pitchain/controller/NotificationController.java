package com.pitchain.controller;

import com.pitchain.jwt.MemberDetails;
import com.pitchain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(value = "/notifications", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal MemberDetails memberDetails) {
        return notificationService.subscribe(memberDetails.id());
    }
}
