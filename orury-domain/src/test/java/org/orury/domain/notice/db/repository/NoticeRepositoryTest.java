package org.orury.domain.notice.db.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.admin.domain.entity.Admin;
import org.orury.domain.admin.infrastructure.AdminRepository;
import org.orury.domain.config.RepositoryTest;
import org.orury.domain.notice.domain.entity.Notice;
import org.orury.domain.notice.infrastructure.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.orury.domain.DomainFixtureFactory.TestAdmin.createAdmin;

@Disabled
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
        Admin admin = adminRepository.save(createAdmin().build().get());
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
}