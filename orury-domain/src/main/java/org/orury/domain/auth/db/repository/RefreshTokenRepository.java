package org.orury.domain.auth.db.repository;

import org.orury.domain.auth.db.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByValue(String value);
}
