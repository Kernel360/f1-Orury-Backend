package org.orury.domain.crew.domain.dto;

import jakarta.persistence.AttributeConverter;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.InfraImplException;

import java.util.Arrays;
import java.util.Objects;

public class CrewGenderConverter implements AttributeConverter<CrewGender, String> {
    @Override
    public String convertToDatabaseColumn(CrewGender attribute) {
        return attribute.getCode();
    }

    @Override
    public CrewGender convertToEntityAttribute(String dbData) {
        return Arrays.stream(CrewGender.values())
                .filter(it -> Objects.equals(it.getCode(), dbData))
                .findFirst()
                .orElseThrow(() -> new InfraImplException(CrewErrorCode.INVALID_GENDER));
    }
}
