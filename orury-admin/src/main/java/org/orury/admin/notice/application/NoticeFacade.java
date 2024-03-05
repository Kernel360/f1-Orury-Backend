package org.orury.admin.notice.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.admin.admin.application.AdminService;
import org.orury.admin.notice.interfaces.request.NoticeRequest;
import org.orury.domain.notice.domain.NoticeService;
import org.orury.domain.notice.domain.dto.NoticeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeFacade {
    private final NoticeService noticeService;
    private final AdminService adminService;

    public void createNotice(Long adminId, NoticeRequest request) {
        var admin = adminService.getAdmin(adminId);
        var notice = request.toDto(admin);
        noticeService.createNotice(admin, notice);
    }

    public void updateNotice(Long adminId, NoticeRequest request) {
        var admin = adminService.getAdmin(adminId);
        var oldNotice = noticeService.getNotice(request.id());
        var newNotice = request.toDto(admin, oldNotice);
        noticeService.createNotice(admin, newNotice);
    }

    public void deleteNotice(Long adminId, Long noticeId) {
        adminService.getAdmin(adminId);
        noticeService.deleteNotice(noticeId);
    }

    public NoticeDto getNotice(Long noticeId) {
        return noticeService.getNotice(noticeId);
    }

    public List<NoticeDto> getNotices() {
        return noticeService.getNotices();
    }
}
