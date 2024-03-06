package org.orury.domain.auth.domain;

public interface RefreshTokenStore {
    void save(Long id, String value);

    void delete(Long userId);
}
