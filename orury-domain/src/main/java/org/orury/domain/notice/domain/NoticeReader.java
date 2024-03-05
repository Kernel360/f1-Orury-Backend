package org.orury.domain.notice.domain;

import org.orury.domain.notice.domain.entity.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeReader {
    Optional<Notice> getNotice(Long noticeId);

    List<Notice> getNotices();
}
