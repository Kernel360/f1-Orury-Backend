package org.orury.client.notification.application;

import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long userId, String lastEventId);

    void send(UserDto userDto, String content, String url);
}
