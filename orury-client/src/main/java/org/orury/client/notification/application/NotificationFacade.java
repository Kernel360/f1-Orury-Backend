package org.orury.client.notification.application;

import static org.orury.domain.global.constants.NumberConstants.NOTIFICATION_PAGINATION_SIZE;

import org.orury.client.notification.interfaces.response.NotificationResponse;
import org.orury.client.user.application.UserService;
import org.orury.domain.notification.domain.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationFacade {
    private final NotificationService notificationService;
    private final UserService userService;

    public Page<NotificationResponse> getNotifications(Long userId, int page) {
        var pageRequest = PageRequest.of(page, NOTIFICATION_PAGINATION_SIZE);
        Page<NotificationDto> notificationDtos = notificationService.getNofifications(pageRequest, userId);
        return convertNotificationDtosToNotificationResponse(notificationDtos);
    }

    public void changeNotificationRead(Long userId, Long notificationId) {
        NotificationDto notificationDto = notificationService.getNotification(notificationId);
        notificationService.changeNotificationRead(userId, notificationDto);
    }

    public SseEmitter subscribe(Long userId, String lastEventId) {
        return notificationService.subscribe(userId, lastEventId);
    }


    private Page<NotificationResponse> convertNotificationDtosToNotificationResponse(Page<NotificationDto> notificationDtos) {
        return notificationDtos.map(NotificationResponse::of);
    }


}
