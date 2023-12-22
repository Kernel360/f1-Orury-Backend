package org.fastcampus.oruryadmin.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryadmin.domain.notice.db.model.Notice;
import org.fastcampus.oruryadmin.domain.notice.service.NoticeService;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "공지사항 생성", description = "request에 담긴 값(adminid, title, content, images)으로 공지사항을 생성한다.")
    @PostMapping("/notice")
    public ApiResponse<Object> createNotice(@RequestBody Notice){

        return ApiResponse.builder()
                .build();
    }
}
