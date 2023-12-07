package org.fastcampus.oruryadmin.domain.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryadmin.domain.notice.service.NoticeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class NoticeController {
    private final NoticeService noticeService;
}
