package org.fastcampus.orurydomain.notice.db.repository;

import org.fastcampus.orurydomain.notice.db.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
