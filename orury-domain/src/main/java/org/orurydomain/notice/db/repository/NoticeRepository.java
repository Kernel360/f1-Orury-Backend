package org.orurydomain.notice.db.repository;

import org.orurydomain.notice.db.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
