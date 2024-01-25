package org.fastcampus.orurydomain.auth.dto.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoAccount(
        Profile profile,
        String email
) {
    public static KakaoAccount of(
            Profile profile,
            String email
    ) {
        return new KakaoAccount(
                profile,
                email
        );
    }
}
