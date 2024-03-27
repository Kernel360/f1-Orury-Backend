package org.orury.domain.gym.domain.dto;

import jakarta.persistence.AttributeConverter;
import org.orury.common.error.code.GymErrorCode;
import org.orury.common.error.exception.InfraImplException;

import java.util.Arrays;
import java.util.Objects;

public class GymTypeConverter implements AttributeConverter<GymType, String> {
    @Override
    public String convertToDatabaseColumn(GymType attribute) {
        return attribute.getCode();
    }

    @Override
    public GymType convertToEntityAttribute(String dbData) {
        return Arrays.stream(GymType.values())
                .filter(it -> Objects.equals(it.getCode(), dbData))
                .findFirst()
                .orElseThrow(() -> new InfraImplException(GymErrorCode.INVALID_GYM_TYPE));
    }
}
