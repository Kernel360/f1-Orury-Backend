package org.orury.domain.notice.domain;

import org.orury.domain.notice.domain.entity.Notice;

import java.util.List;

public interface NoticeReader {
    Notice getNotice(Long noticeId);

    List<Notice> getNotices();
}
