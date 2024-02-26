package org.orury.domain.notice.db.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.admin.db.model.Admin;
import org.orury.domain.admin.db.repository.AdminRepository;
import org.orury.domain.admin.dto.RoleType;
import org.orury.domain.config.RepositoryTest;
import org.orury.domain.notice.db.model.Notice;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class NoticeRepositoryTest {
    private final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public NoticeRepositoryTest(
            NoticeRepository noticeRepository,
            AdminRepository adminRepository
    ) {
        this.noticeRepository = noticeRepository;
        this.adminRepository = adminRepository;
    }

    @DisplayName("")
    @Test
    void test() {
        Admin admin = adminRepository.save(createAdmin());
        Notice notice = noticeRepository.save(Notice.of(
                "title",
                "content",
                admin
        ));

        assertThat(notice)
                .hasFieldOrPropertyWithValue("title", "title")
                .hasFieldOrPropertyWithValue("content", "content");
        assertThat(admin)
                .hasFieldOrPropertyWithValue("name", "name")
                .hasFieldOrPropertyWithValue("email", "email")
                .hasFieldOrPropertyWithValue("password", "pw");
    }

    private Admin createAdmin() {
        return Admin.of(
                "name",
                "email",
                "pw",
                RoleType.ROLE_ADMIN
        );

    }
}