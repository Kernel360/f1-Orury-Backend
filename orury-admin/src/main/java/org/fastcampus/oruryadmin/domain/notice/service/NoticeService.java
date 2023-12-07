package org.fastcampus.oruryadmin.domain.notice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryadmin.domain.notice.db.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public void save() {

    }
}
