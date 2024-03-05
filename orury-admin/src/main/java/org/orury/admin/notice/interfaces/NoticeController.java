package org.orury.admin.notice.interfaces;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.admin.global.security.dto.login.response.AdminPrincipal;
import org.orury.admin.notice.application.NoticeFacade;
import org.orury.admin.notice.interfaces.request.NoticeRequest;
import org.orury.domain.base.converter.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
@RestController
public class NoticeController {
    private final NoticeFacade noticeFacade;

    @Operation(summary = "공지사항 생성", description = "request에 담긴 값(id, title, content)으로 공지사항을 생성한다.")
    @PostMapping
    public ApiResponse createNotice(
            @AuthenticationPrincipal AdminPrincipal principal,
            @RequestBody NoticeRequest request
    ) {
        noticeFacade.createNotice(principal.id(), request);
        return ApiResponse.of("공지사항 생성");
    }

    @Operation(summary = "공지사항 수정", description = "request에 담긴 값(id, title, content)으로 공지사항을 수정한다.")
    @PutMapping
    public ApiResponse updateNotice(
            @AuthenticationPrincipal AdminPrincipal principal,
            @RequestBody NoticeRequest request
    ) {
        noticeFacade.updateNotice(principal.id(), request);
        return ApiResponse.of("공지사항 수정");
    }

    @Operation(summary = "공지사항 삭제", description = "noticeId에 담긴 값으로 공지사항을 삭제한다.")
    @DeleteMapping("/{noticeId}")
    public ApiResponse deleteNotice(
            @AuthenticationPrincipal AdminPrincipal principal,
            @PathVariable Long noticeId
    ) {
        noticeFacade.deleteNotice(principal.id(), noticeId);
        return ApiResponse.of("공지사항 삭제");
    }

    @Operation(summary = "공지사항 조회", description = "noticeId에 담긴 값으로 공지사항을 조회한다.")
    @GetMapping("/{noticeId}")
    public ApiResponse getNotice(
            @PathVariable Long noticeId
    ) {
        var notice = noticeFacade.getNotice(noticeId);
        return ApiResponse.of("공지사항 조회", notice);
    }

    @Operation(summary = "공지사항 전체 조회", description = "공지사항을 전체 조회한다.")
    @GetMapping
    public ApiResponse getNotice() {
        var notices = noticeFacade.getNotices();
        return ApiResponse.of("공시사항 전체 조회", notices);
    }
}
