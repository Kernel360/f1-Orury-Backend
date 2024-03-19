package org.orury.client.notification.interfaces.response;

import org.orury.domain.notification.domain.dto.NotificationDto;

public record NotificationResponse(
        Long id,
        Long userId,
        String title,
        String content,
        String url

) {
    public static NotificationResponse of(NotificationDto dto) {
        return new NotificationResponse(
                dto.id(),
                dto.userDto().id(),
                dto.title(),
                dto.content(),
                dto.url()
        );
    }
}
