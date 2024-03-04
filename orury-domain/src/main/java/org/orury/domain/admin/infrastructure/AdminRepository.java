package org.orury.domain.admin.infrastructure;

import org.orury.domain.admin.domain.entity.Admin;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);

    @EntityGraph(attributePaths = "authorities")
    Optional<Admin> findOneWithAuthoritiesByName(String name);
}
