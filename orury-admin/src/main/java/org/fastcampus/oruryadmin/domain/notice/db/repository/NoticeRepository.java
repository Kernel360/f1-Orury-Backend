package org.fastcampus.oruryadmin.domain.notice.db.repository;

import org.fastcampus.oruryadmin.domain.notice.db.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
