package org.orury.client.notification.interfaces.message;

import lombok.Getter;

@Getter
public enum NotificationMessage {
    NOTIFICATIONS_READ("알림함을 조회했습니다."),
    NOTIFICATION_STATUS_READ("알림을 읽음 표시 했습니다");

    private final String message;

    NotificationMessage(String message) {
        this.message = message;
    }
}
