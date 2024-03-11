package org.orury.domain.crew.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CrewGender {
    ANY("ANY_GENDER", "모든 성별", "A"),
    FEMALE("FEMALE", "여성", "F"),
    MALE("MALE", "남성", "M");

    private final String status;
    private final String description;
    private final String code;
}
