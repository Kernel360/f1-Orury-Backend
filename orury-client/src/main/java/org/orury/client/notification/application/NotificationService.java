package org.orury.client.notification.application;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(Long userId);

    void notify(Long userId, Object event);

    void sendToClient(Long id, Object data);


}
