package org.orury.client.notification.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.orury.client.notification.application.NotificationService;
import org.orury.domain.notification.infrastructure.EmitterRepositoryImpl;
import org.orury.domain.user.domain.dto.UserPrincipal;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notification")
@RestController
public class NotificationController {
    private final NotificationService notificationService;

    // 서버 메모리에 저장된 emitter, event cache를 파악하기 위해 임시로 추가했습니다.
    private final EmitterRepositoryImpl emitterRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
                                String lastEventId) {
        return notificationService.subscribe(userPrincipal.id(), lastEventId);
    }

    // 서버 메모리에 있는 emitter를 확인하기 위해 임시로 추가한 api 입니다.
    @GetMapping(value = "/getemitter")
    public String getAllEmitter(@AuthenticationPrincipal UserPrincipal userPrincipal) throws JsonProcessingException {

        Map<String, SseEmitter> map = emitterRepository.getAllEmitters();

        return objectMapper.writeValueAsString(map);
    }

    // 서버 메모리에 있는 event를 확인하기 위해 임시로 추가한 api 입니다.
    @GetMapping(value = "/getevent")
    public String getAll(@AuthenticationPrincipal UserPrincipal userPrincipal) throws JsonProcessingException {

        Map<String, Object> map = emitterRepository.getAllEvents();

        return objectMapper.writeValueAsString(map);
    }
}
