package org.orury.domain.crew.domain.dto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.InfraImplException;

import java.util.Arrays;
import java.util.Objects;

@Converter
public class CrewStatusConverter implements AttributeConverter<CrewStatus, String> {
    @Override
    public String convertToDatabaseColumn(CrewStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public CrewStatus convertToEntityAttribute(String dbData) {
        return Arrays.stream(CrewStatus.values())
                .filter(it -> Objects.equals(it.getCode(), dbData))
                .findFirst()
                .orElseThrow(() -> new InfraImplException(CrewErrorCode.INVALID_STATUS));
    }
}
