package org.orury.domain.crew.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.orury.domain.global.constants.NumberConstants;

@Getter
@AllArgsConstructor
public enum CrewGender {
    ANY("모든 성별", NumberConstants.ANY_SEX, "A"),
    FEMALE("여성", NumberConstants.FEMALE, "F"),
    MALE("남성", NumberConstants.MALE, "M");

    private final String description;
    private final int userCode;
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
