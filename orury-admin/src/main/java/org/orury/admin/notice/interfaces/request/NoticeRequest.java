package org.orury.admin.notice.interfaces.request;

import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.notice.domain.dto.NoticeDto;

public record NoticeRequest(
        Long id,
        String title,
        String content
) {
    public static NoticeRequest of(
            String title,
            String content
    ) {
        return NoticeRequest.of(null, title, content);
    }

    public static NoticeRequest of(
            Long id,
            String title,
            String content
    ) {
        return new NoticeRequest(id, title, content);
    }

    public NoticeDto toDto(AdminDto admin) {
        return NoticeDto.of(
                id,
                title,
                content,
                admin,
                null,
                null
        );
    }

    public NoticeDto toDto(AdminDto admin, NoticeDto notice) {
        return NoticeDto.of(
                id,
                title,
                content,
                admin,
                notice.createdAt(),
                null
        );
    }
}
