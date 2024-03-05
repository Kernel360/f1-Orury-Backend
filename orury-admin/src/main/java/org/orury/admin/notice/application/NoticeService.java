package org.orury.admin.notice.application;


import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.notice.domain.dto.NoticeDto;

import java.util.List;

public interface NoticeService {

    void createNotice(AdminDto adminDto, NoticeDto noticeDto);

    void deleteNotice(Long noticeId);

    NoticeDto getNotice(Long noticeId);

    List<NoticeDto> getNotices();

}
