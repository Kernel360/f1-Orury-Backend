package org.orury.domain.notification.infrastructure;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void saveEventCache(String eventCacheId, Object event);

    Map<String, SseEmitter> findAllEmitterStartWithByUserId(Long userId);

    Map<String, Object> findAllEventCacheStartWithByUserId(Long userId);

    void deleteEmitterById(String emitterId);

    void deleteAllEmitterStartWithUserId(String userId);

    void deleteAllEventCacheStartWithUserId(String userId);

    Map<String, SseEmitter> getAllEmitters();

    Map<String, Object> getAllEvents();
}
