package org.orury.domain.auth.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.auth.domain.RefreshTokenReader;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenReaderImpl implements RefreshTokenReader {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public boolean existsByValue(String value) {
        return refreshTokenRepository.existsByValue(value);
    }
}
