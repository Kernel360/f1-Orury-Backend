package org.orury.client.notification.application;

import org.apache.logging.log4j.util.Strings;
import org.orury.client.notification.interfaces.response.NotificationResponse;
import org.orury.domain.notification.domain.dto.NotificationDto;
import org.orury.domain.notification.domain.entity.Notification;
import org.orury.domain.notification.infrastructure.EmitterRepository;
import org.orury.domain.notification.infrastructure.NotificationRepository;
import org.orury.domain.user.domain.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notifyRepository;
    private final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public SseEmitter subscribe(Long userId, String lastEventId) {
        String emitterId = makeTimeIncludeId(userId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // emitter가 연결이 끊겼거나, 타임아웃 됐을 때 삭제될 수 있도록 함. (비동기)
        emitter.onCompletion(() -> {
            logger.info("알람테스트 : emitter.onCompletion으로 emitter 삭제됨.");
            emitterRepository.deleteEmitterById(emitterId);
        });
        emitter.onTimeout(() -> {
            logger.info("알람테스트 : emitter.onTimeout으로 emitter 삭제됨.");
            emitterRepository.deleteEmitterById(emitterId);
        });

        // 503 에러 방지 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(userId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }
        logger.info("알람테스트 : subscribe 실행되었음. ");
        return emitter;
    }

    @Override
    public void send(UserDto userDto, String content, String url) {
        NotificationDto notificationDto = NotificationDto.of(null, userDto, content, url, 0, null, null);
        Notification notification = notifyRepository.save(notificationDto.toEntity());

        String eventId = makeTimeIncludeId(userDto.id());
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(userDto.id());
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationResponse.of(notificationDto, notification.getId()));
                    logger.info("알람테스트 :sendNotification 실행. emitter: " + emitter + " / eventId: " + eventId + " / key: " + key + " / notification: " + notification);
                }
        );
    }

    private String makeTimeIncludeId(Long userId) {
        return userId.toString() + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            // Emitter에 새로운 이벤트 생성하여 전송
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            // TODO: 후에 CustomException 으로 변경
            emitterRepository.deleteEmitterById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return Strings.isNotEmpty(lastEventId);
    }

    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(userId);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> {
                    sendNotification(emitter, entry.getKey(), emitterId, entry.getValue());
                    logger.info("알람테스트 : sendLostData 내의 sendNotification 실행. emitter: " + emitter + " / lasteventId: " + lastEventId);
                });
    }
}