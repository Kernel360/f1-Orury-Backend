package org.fastcampus.orurydomain.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Profile(
        String nickname
) {
    public static Profile of(
            String nickname
    ) {
        return new Profile(
                nickname
        );
    }
}
