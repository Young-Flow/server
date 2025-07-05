package com.pitchain.notificationhistory.infrastucture;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
    }

    public void deleteById(String id) {
        emitters.remove(id);
    }

    public Map<String, SseEmitter> findAllStartWithById(String memberId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
