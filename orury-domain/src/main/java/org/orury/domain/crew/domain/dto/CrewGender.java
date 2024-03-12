package org.orury.domain.crew.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CrewGender {
    ANY("모든 성별", "A"),
    FEMALE("여성", "F"),
    MALE("남성", "M");

    private final String description;
    private final String code;

    @JsonCreator
    public static CrewGender getEnumFromValue(String value) {
        for (CrewGender gender : CrewGender.values()) {
            if (gender.name().equals(value)) {
                return gender;
            }
        }
        return null;
    }
}
