package org.fastcampus.oruryclient.gym.converter.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GymSearchRequest(
        String searchWord,
        float latitude,
        float longitude
) {
}
