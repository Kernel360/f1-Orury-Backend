package org.orury.domain.admin.domain.dto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Converter
public class RoleTypesConverter implements AttributeConverter<Set<RoleType>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<RoleType> attribute) {
        return attribute.stream()
                .map(RoleType::name)
                .sorted()
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<RoleType> convertToEntityAttribute(String dbData) {
        var set = Arrays.stream(dbData.split(DELIMITER)).map(RoleType::valueOf).collect(Collectors.toSet());
        log.info("set : {}", set);
        return Arrays.stream(dbData.split(DELIMITER))
                .map(RoleType::valueOf)
                .collect(Collectors.toSet());
    }
}