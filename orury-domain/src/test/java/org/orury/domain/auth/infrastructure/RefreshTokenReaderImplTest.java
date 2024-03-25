package org.orury.domain.auth.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.config.InfrastructureTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

@DisplayName("[Reader] 리프레시토큰 ReaderImpl 테스트")
class RefreshTokenReaderImplTest extends InfrastructureTest {

    @Test
    void existsByValue() {
        // given & when
        refreshTokenReader.existsByValue("testRefreshTokenValue");

        // then
        then(refreshTokenRepository).should(only())
                .existsByValue(any(String.class));
    }
}