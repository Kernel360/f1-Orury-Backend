package org.orury.domain.notice.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.notice.domain.dto.NoticeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class NoticeServiceImpl implements NoticeService {
    private final NoticeReader noticeReader;
    private final NoticeStore noticeStore;

    @Override
    public void createNotice(AdminDto admin, NoticeDto noticeDto) {
        noticeStore.save(noticeDto.toEntity(admin.toEntity()));
    }

    @Override
    public void deleteNotice(Long noticeId) {
        noticeStore.delete(noticeId);
    }

    @Override
    public NoticeDto getNotice(Long noticeId) {
        return NoticeDto.from(noticeReader.getNotice(noticeId));
    }

    @Override
    public List<NoticeDto> getNotices() {
        return noticeReader.getNotices().stream().map(NoticeDto::from).toList();
    }
}
