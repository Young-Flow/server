package com.pitchain.common.redis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.EventType;
import com.pitchain.common.constant.NotificationType;
import com.pitchain.common.constant.SpStatus;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.common.payload.event.Event;
import com.pitchain.common.payload.event.TranscodingEventPayload;
import com.pitchain.sp.domain.Sp;
import com.pitchain.notificationhistory.application.NotificationHistoryService;
import com.pitchain.sp.application.SpService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SpService spService;
    private final NotificationHistoryService notificationHistoryService;

    private static JavaType javaType;

    @PostConstruct
    public void init() {
        javaType = objectMapper.getTypeFactory().constructParametricType(Event.class, TranscodingEventPayload.class);
    }

    @Transactional
    public void onMessage(String message) {
        try {
            Event<TranscodingEventPayload> event = objectMapper.readValue(message, javaType);

            TranscodingEventPayload payload = event.getPayload();
            Long spId = payload.getSpId();

            Sp sp = spService.getSp(spId);

            updateSpStatus(sp.getId(), event.getEventType());

            sendNotificationLog(sp.getId(), event.getEventType());
        } catch (JsonProcessingException e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }

    private void updateSpStatus(Long spId, EventType eventType) {
        SpStatus spStatus = SpStatus.fromEventType(eventType);
        spService.updateStatus(spId, spStatus);
    }

    private void sendNotificationLog(Long spId, EventType eventType) {
        NotificationType notificationType = NotificationType.fromEventType(eventType);
        notificationHistoryService.send(spId, notificationType);
    }

}
