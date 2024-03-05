package org.orury.domain.notice.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.notice.domain.NoticeReader;
import org.orury.domain.notice.domain.entity.Notice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class NoticeReaderImpl implements NoticeReader {
    private final NoticeRepository noticeRepository;

    @Override
    public Optional<Notice> getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId);
    }

    @Override
    public List<Notice> getNotices() {
        return noticeRepository.findAll();
    }
}
