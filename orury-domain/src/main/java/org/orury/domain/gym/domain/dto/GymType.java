package org.orury.domain.gym.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GymType {
    BOULDERING("볼더링", "B"),
    WALL("인공 암벽", "W");

    private final String description;
    private final String code;

    @JsonCreator
    public static GymType getEnumFromValue(String value) {
        for (GymType gypType : GymType.values()) {
            if (gypType.name().equals(value)) {
                return gypType;
            }
        }
        return null;
    }
}
