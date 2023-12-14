package org.fastcampus.oruryadmin.domain.admin.converter.dto;

import java.time.LocalDateTime;

/**
 * DTO for {@link org.fastcampus.oruryadmin.domain.notice.db.model.Notice}
 */
public record NoticeDto(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static NoticeDto of(
            String title,
            String content
    ) {
        return NoticeDto.of(
                null,
                title,
                content,
                null,
                null
        );
    }

    public static NoticeDto of(
            Long id,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new NoticeDto(
                id,
                title,
                content,
                createdAt,
                updatedAt
        );
    }
}