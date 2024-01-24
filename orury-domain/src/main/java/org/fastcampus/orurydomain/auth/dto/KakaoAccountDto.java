package org.fastcampus.orurydomain.auth.dto;

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
    public static KakaoAccountDto of(
            Long id,
            boolean hasSignedUp,
            LocalDateTime connectedAt,
            LocalDateTime synchedAt,
            KakaoAccount kakaoAccount
    ) {
        return new KakaoAccountDto(
                id,
                hasSignedUp,
                connectedAt,
                synchedAt,
                kakaoAccount
        );
    }
}
