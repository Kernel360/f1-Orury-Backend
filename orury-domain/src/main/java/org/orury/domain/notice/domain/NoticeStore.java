package org.orury.domain.notice.domain;

import org.orury.domain.notice.domain.entity.Notice;

public interface NoticeStore {
    void delete(Long noticeId);

    void save(Notice notice);
}
