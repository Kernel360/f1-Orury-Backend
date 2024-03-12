package org.orury.domain.global.domain;

import jakarta.persistence.AttributeConverter;
import org.orury.common.error.exception.InfraImplException;

import static org.orury.common.error.code.CrewErrorCode.INVALID_REGION;

public class RegionConverter implements AttributeConverter<Region, String> {
    @Override
    public String convertToDatabaseColumn(Region region) {
        return region.getDescription();
    }

    @Override
    public Region convertToEntityAttribute(String dbData) {
        for (Region region : Region.values()) {
            if (region.getDescription().equals(dbData)) {
                return region;
            }
        }
        throw new InfraImplException(INVALID_REGION);
    }
}
