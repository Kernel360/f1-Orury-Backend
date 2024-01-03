package org.fastcampus.orurydomain.admin.db.repository;

import org.fastcampus.orurydomain.admin.db.model.Admin;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    @EntityGraph(attributePaths = "authorities")
    Optional<Admin> findOneWithAuthoritiesByName(String name);
}
