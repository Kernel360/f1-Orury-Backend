package org.orury.domain.auth.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.auth.domain.entity.RefreshToken;
import org.orury.domain.config.InfrastructureTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.internal.verification.VerificationModeFactory.only;

@DisplayName("[Store] 리프레시토큰 StoreImpl 테스트")
class RefreshTokenStoreImplTest extends InfrastructureTest {

    @Test
    void save() {
        // given & when
        refreshTokenStore.save(25781L, "testRefreshTokenValue");

        // then
        then(refreshTokenRepository).should(only())
                .save(any(RefreshToken.class));
    }

    @Test
    void delete() {
        // given & when
        refreshTokenStore.delete(anyLong());

        // then
        then(refreshTokenRepository).should(only())
                .deleteByUserId(anyLong());
    }
}