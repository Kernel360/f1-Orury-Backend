package org.orury.domain.notice.db.repository;

import org.orury.domain.notice.db.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
