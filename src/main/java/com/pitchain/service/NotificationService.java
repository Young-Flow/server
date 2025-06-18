package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.NotificationType;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.common.payload.notification.Notification;
import com.pitchain.common.payload.notification.NotificationPayload;
import com.pitchain.common.payload.notification.TranscodingNotificationPayload;
import com.pitchain.entity.Member;
import com.pitchain.entity.NotificationLog;
import com.pitchain.entity.Sp;
import com.pitchain.repository.EmitterRepository;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final EntityFacade entityFacade;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
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

    public void send(Long spId, NotificationType notificationType) {
        Sp sp = entityFacade.getSp(spId);
        Member receiver = entityFacade.getMember(sp.getBm().getCompany().getMember().getId());

        NotificationLog notificationLog = saveNotificationLog(notificationType, receiver);
        TranscodingNotificationPayload notificationPayload = TranscodingNotificationPayload.of(sp.getId(), notificationLog);
        Notification notification = Notification.of(notificationType, notificationPayload);

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(String.valueOf(receiver.getId()));
        sseEmitters.forEach(
                (key, emitter) -> sendToClient(emitter, key, notification)
        );
    }

    private NotificationLog saveNotificationLog(NotificationType notificationType, Member receiver) {
        NotificationLog notificationLog = NotificationLog.of(receiver, notificationType);
        notificationRepository.save(notificationLog);
        return notificationLog;
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
