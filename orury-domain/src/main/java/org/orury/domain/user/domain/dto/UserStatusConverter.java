package org.orury.domain.user.domain.dto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.UserErrorCode;
import org.orury.common.error.exception.InfraImplException;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Converter
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {
    @Override
    public String convertToDatabaseColumn(UserStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public UserStatus convertToEntityAttribute(String dbData) {
        return Arrays.stream(UserStatus.values())
                .filter(it -> Objects.equals(it.getCode(), dbData))
                .findFirst()
                .orElseThrow(() -> new InfraImplException(UserErrorCode.INVALID_USER));
    }
}