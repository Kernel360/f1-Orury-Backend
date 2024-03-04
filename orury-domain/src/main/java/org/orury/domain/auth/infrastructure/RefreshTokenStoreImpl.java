package org.orury.domain.auth.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.auth.domain.RefreshTokenStore;
import org.orury.domain.auth.domain.entity.RefreshToken;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class RefreshTokenStoreImpl implements RefreshTokenStore {
    RefreshTokenRepository refreshTokenRepository;

    @Override
    public void save(Long id, String value) {
        RefreshToken refreshToken = RefreshToken.of(id, value, LocalDateTime.now(), LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
    }
}
