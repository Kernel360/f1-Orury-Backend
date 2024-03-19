package org.orury.client.notification.interfaces;

import static org.orury.client.notification.interfaces.message.NotificationMessage.NOTIFICATIONS_READ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.orury.client.notification.application.NotificationFacade;
import org.orury.client.notification.application.NotificationService;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.notification.infrastructure.EmitterRepositoryImpl;
import org.orury.domain.user.domain.dto.UserPrincipal;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notification")
@RestController
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationFacade notificationFacade;

    // 서버 메모리에 저장된 emitter, event cache를 파악하기 위해 임시로 추가했습니다.
    private final EmitterRepositoryImpl emitterRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
                                String lastEventId) {
        return notificationService.subscribe(userPrincipal.id(), lastEventId);
    }

    @Operation(summary = "알림함 조회", description = "알림함을 조회한다.")
    @GetMapping()
    public ApiResponse getNotification(@RequestParam int page, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        var response = notificationFacade.getAllNotification(userPrincipal.id(), page);
        return ApiResponse.of(NOTIFICATIONS_READ.getMessage(), response);
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
