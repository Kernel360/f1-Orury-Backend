package org.fastcampus.orurydomain.auth.db.repository;

import org.fastcampus.orurydomain.auth.db.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByValue(String value);
}
