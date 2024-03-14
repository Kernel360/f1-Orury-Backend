package org.orury.client.notification.interfaces.response;

import org.orury.domain.notification.domain.dto.NotificationDto;

public record NotificationResponse(
        Long id,
        Long userId,
        String content,
        String url

) {
    public static NotificationResponse of(NotificationDto dto, Long id) {
        return new NotificationResponse(
                dto.id(),
                dto.userDto().id(),
                dto.content(),
                dto.url()
        );
    }
}
