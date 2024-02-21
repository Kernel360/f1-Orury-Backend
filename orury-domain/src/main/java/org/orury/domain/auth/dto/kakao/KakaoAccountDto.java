package org.orury.domain.auth.dto.kakao;

import java.time.LocalDateTime;

public record KakaoAccountDto(
        Long id,
        boolean hasSignedUp,
        LocalDateTime connectedAt,
        LocalDateTime synchedAt,
        KakaoAccount kakaoAccount
) {
}
