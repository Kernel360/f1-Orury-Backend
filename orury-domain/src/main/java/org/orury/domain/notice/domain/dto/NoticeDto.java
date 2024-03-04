package org.orury.domain.notice.domain.dto;


import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.admin.domain.entity.Admin;
import org.orury.domain.notice.domain.entity.Notice;

import java.time.LocalDateTime;

/**
 * DTO for {@link Notice}
 */
public record NoticeDto(
        Long id,
        String title,
        String content,
        AdminDto admin,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static NoticeDto of(
            Long id,
            String title,
            String content,
            AdminDto admin,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new NoticeDto(
                id,
                title,
                content,
                admin,
                createdAt,
                updatedAt
        );
    }

    public static NoticeDto from(Notice entity) {
        return NoticeDto.of(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                AdminDto.from(entity.getAdmin()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Notice toEntity(Admin admin) {
        return Notice.of(
                title,
                content,
                admin
        );
    }
}