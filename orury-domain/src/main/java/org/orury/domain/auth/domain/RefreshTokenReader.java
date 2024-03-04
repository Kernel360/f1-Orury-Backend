package org.orury.domain.auth.domain;

public interface RefreshTokenReader {
    boolean existsByValue(String value);
}
