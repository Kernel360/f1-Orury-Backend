package org.orury.client.notification.interfaces;

import org.orury.client.notification.application.NotificationService;
import org.orury.domain.user.domain.dto.UserPrincipal;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notification")
@RestController
public class NotificationController {
    private final NotificationService notificationService;


    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return notificationService.subscribe(userPrincipal.id());
    }

    @PostMapping("/send-data/{id}")
    public void sendData(@PathVariable Long id) {
        notificationService.notify(id, "test event");
    }
}
