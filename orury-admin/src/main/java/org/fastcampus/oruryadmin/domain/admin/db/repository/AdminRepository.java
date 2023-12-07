package org.fastcampus.oruryadmin.domain.admin.db.repository;

import org.fastcampus.oruryadmin.domain.admin.db.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
