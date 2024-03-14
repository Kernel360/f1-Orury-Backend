package org.orury.domain.notification.domain.dto;

import org.orury.domain.notification.domain.entity.Notification;
import org.orury.domain.user.domain.dto.UserDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link Notification}
 */
public record NotificationDto(
        Long id,
        UserDto userDto,
        String content,
        String url,
        int isRead,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static NotificationDto of(
            Long id,
            UserDto userDto,
            String content,
            String url,
            int isRead,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new NotificationDto(
                id,
                userDto,
                content,
                url,
                isRead,
                createdAt,
                updatedAt
        );
    }

    public static NotificationDto from(Notification entity) {
        return NotificationDto.of(
                entity.getId(),
                UserDto.from(entity.getUser()),
                entity.getContent(),
                entity.getUrl(),
                entity.getIsRead(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Notification toEntity() {
        return Notification.of(
                id,
                userDto.toEntity(),
                content,
                url,
                isRead,
                createdAt,
                updatedAt
        );
    }
}
