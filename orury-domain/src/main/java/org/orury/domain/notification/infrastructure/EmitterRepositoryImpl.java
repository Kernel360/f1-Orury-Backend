package org.orury.domain.notification.infrastructure;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByUserId(Long userId) {
        // 해당 회원과 관련된 모든 Emitter 조회
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId.toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByUserId(Long userId) {
        // 해당 회원과 관련된 모든 EventCache 조회
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId.toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    // TODO : 맵에 저장된 모든 Emitter 조회하는 메소드 구현
    // TODO : 맵에 저장된 모든 EventCache 조회하는 메소드 구현

    @Override
    public void deleteEmitterById(String emitterId) {
        emitters.remove(emitterId);
    }

    @Override
    public void deleteAllEmitterStartWithUserId(String userId) {
        // 해당 회원과 관련된 모든 Emitter 삭제
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(userId.toString())) {
                        emitters.remove(key);
                    }
                }
        );
    }

    @Override
    public void deleteAllEventCacheStartWithUserId(String userId) {
        // 해당 회원과 관련된 모든 EventCache 삭제
        // 회원 탈퇴 등에 사용
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(userId.toString())) {
                        eventCache.remove(key);
                    }
                }
        );
    }

    // TODO:  Notification의 isRead가 1이 되었을 때 해당 Notification을 가지고 있는 모든 이벤트 캐시도 삭제될 수 있는 메소드 추가하기
}
