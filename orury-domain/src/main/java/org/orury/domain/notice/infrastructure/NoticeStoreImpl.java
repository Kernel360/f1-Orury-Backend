package org.orury.domain.notice.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.notice.domain.NoticeStore;
import org.orury.domain.notice.domain.entity.Notice;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NoticeStoreImpl implements NoticeStore {
    private final NoticeRepository noticeRepository;

    @Override
    public void delete(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }

    @Override
    public void save(Notice notice) {
        noticeRepository.save(notice);
    }
}
