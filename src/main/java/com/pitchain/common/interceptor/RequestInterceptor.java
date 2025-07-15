package com.pitchain.common.interceptor;

import com.pitchain.common.constant.SessionType;
import com.pitchain.common.payload.discord.LongTimeRequestDiscordPayload;
import com.pitchain.common.util.LoggingUtil;
import com.pitchain.common.util.RequestInfoExtractor;
import com.pitchain.common.webhook.DiscordWebhookSender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Profile("prod")
@RequiredArgsConstructor
@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final DiscordWebhookSender discordWebhookSender;

    private final static Long MAX_TURNAROUND_TIME_SEC = 60L; // 1min

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String requestId = UUID.randomUUID().toString();
        request.setAttribute(SessionType.REQUEST_ID, requestId);
        request.setAttribute(SessionType.START_TIME, System.currentTimeMillis());
        LoggingUtil.logPreRequest(requestId, request.getRequestURI(), request.getMethod());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute(SessionType.START_TIME);
        long turnaroundTimeSec = (System.currentTimeMillis() - startTime) / 1000;

        if (turnaroundTimeSec > MAX_TURNAROUND_TIME_SEC) {
            sendLongTimeRequestWebhook(turnaroundTimeSec);
        }

        LoggingUtil.logCompleteRequest(response.getStatus(), turnaroundTimeSec, ex);
    }

    private void sendLongTimeRequestWebhook(long turnaroundTime) {
        String requestId = RequestInfoExtractor.getRequestIdInSession();
        String requestFullPath = RequestInfoExtractor.getFullPath();

        discordWebhookSender.send(
                LongTimeRequestDiscordPayload.of(requestId, requestFullPath, turnaroundTime)
        );
    }
}
