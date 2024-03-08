package org.orury.domain.global.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Converter
public class EntityImageConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

    @Override
    public String convertToDatabaseColumn(List<String> images) {
        try {
            return mapper.writeValueAsString(images);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String images) {
        try {
            return mapper.readValue(images, List.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
