package org.fastcampus.orurydomain.auth.dto.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoAccountDto(
        Long id,
        boolean hasSignedUp,
        LocalDateTime connectedAt,
        LocalDateTime synchedAt,
        KakaoAccount kakaoAccount
) {
}
