package org.orury.admin.notice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.NoticeErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.notice.domain.NoticeReader;
import org.orury.domain.notice.domain.NoticeStore;
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
    public void createNotice(AdminDto adminDto, NoticeDto noticeDto) {
        var notice = noticeDto.toEntity(adminDto.toEntity());
        noticeStore.save(notice);
    }

    @Override
    public void deleteNotice(Long noticeId) {
        noticeStore.delete(noticeId);
    }

    @Override
    public NoticeDto getNotice(Long noticeId) {
        var notice = noticeReader.getNotice(noticeId)
                .orElseThrow(() -> new BusinessException(NoticeErrorCode.NOT_FOUND));
        return NoticeDto.from(notice);
    }

    @Override
    public List<NoticeDto> getNotices() {
        return noticeReader.getNotices().stream().map(NoticeDto::from).toList();
    }
}
