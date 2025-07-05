package com.pitchain.notificationhistory.application;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.NotificationType;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.common.payload.notification.Notification;
import com.pitchain.common.payload.notification.NotificationPayload;
import com.pitchain.common.payload.notification.TranscodingNotificationPayload;
import com.pitchain.member.domain.Member;
import com.pitchain.notificationhistory.domain.NotificationHistory;
import com.pitchain.sp.domain.Sp;
import com.pitchain.notificationhistory.infrastucture.EmitterRepository;
import com.pitchain.common.application.EntityFacade;
import com.pitchain.notificationhistory.infrastucture.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class NotificationHistoryService {
    private final EntityFacade entityFacade;
    private final EmitterRepository emitterRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(Long memberId) {
        String sseId = String.valueOf(memberId);

        SseEmitter emitter = saveSseEmitter(sseId);

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, sseId, Notification.ofSuccessSubscribe(memberId));

        return emitter;
    }

    private SseEmitter saveSseEmitter(String sseId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitter.onCompletion(() -> emitterRepository.deleteById(sseId));
        emitter.onTimeout(() -> emitterRepository.deleteById(sseId));
        emitterRepository.save(sseId, emitter);
        return emitter;
    }

    @Transactional
    public void send(Long spId, NotificationType notificationType) {
        Sp sp = entityFacade.getSp(spId);
        Member receiver = entityFacade.getMember(sp.getBm().getCompany().getMember().getId());

        NotificationHistory notificationHistory = saveNotificationLog(notificationType, receiver);
        TranscodingNotificationPayload notificationPayload = TranscodingNotificationPayload.of(sp.getId(), notificationHistory);
        Notification notification = Notification.of(notificationType, notificationPayload);

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(String.valueOf(receiver.getId()));
        sseEmitters.forEach(
                (key, emitter) -> sendToClient(emitter, key, notification)
        );
    }

    private NotificationHistory saveNotificationLog(NotificationType notificationType, Member receiver) {
        NotificationHistory notificationHistory = NotificationHistory.of(receiver, notificationType);
        notificationHistoryRepository.save(notificationHistory);
        return notificationHistory;
    }

    private <T extends NotificationPayload> void sendToClient(SseEmitter emitter, String id, Notification<T> notification) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .id(id)
                            .name("sse")
                            .data(notification)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new GeneralException(ErrorStatus.SSE_SEND_FAILED);
        }
    }
}
