package org.orurydomain.notice.db.repository;

import org.orurydomain.admin.db.model.Admin;
import org.orurydomain.admin.db.repository.AdminRepository;
import org.orurydomain.admin.dto.RoleType;
import org.orurydomain.config.RepositoryTest;
import org.orurydomain.notice.db.model.Notice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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