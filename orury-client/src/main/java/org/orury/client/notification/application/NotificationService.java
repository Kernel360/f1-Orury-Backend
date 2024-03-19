package org.orury.client.notification.application;

import org.orury.domain.notification.domain.dto.NotificationDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long userId, String lastEventId);

    void send(UserDto userDto, String title, String content, String url);

    Page<NotificationDto> getNofification(Pageable pageable, Long id);
}
