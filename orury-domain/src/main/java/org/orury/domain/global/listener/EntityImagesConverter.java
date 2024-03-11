package org.orury.domain.global.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;
import org.orury.common.config.BeanUtils;
import org.orury.common.error.code.FileExceptionCode;
import org.orury.common.error.exception.InfraImplException;
import org.orury.common.util.ImageUtil;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.image.ImageReader;

import java.util.List;

@Converter
public abstract class EntityImagesConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    abstract protected S3Folder domainName();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        // TODO
        // 프론트 null check 로직 추가되면 return값 null로 바꿔야함
        if (ImageUtil.imagesValidation(attribute)) return "[]";
        try {
            var images = attribute.stream().map(ImageUtil::splitUrlToImage).toArray();
            return mapper.writeValueAsString(images);
        } catch (JsonProcessingException e) {
            throw new InfraImplException(FileExceptionCode.FILE_DOWNLOAD_ERROR);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        // TODO
        // 프론트 null check 로직 추가되면 return값 null로 바꿔야함
        if (StringUtils.isBlank(dbData)) return List.of();
        var imageReader = BeanUtils.getBean(ImageReader.class);
        try {
            List<String> images = mapper.readValue(dbData, List.class);
            return imageReader.getImageLinks(domainName(), images);
        } catch (JsonProcessingException e) {
            throw new InfraImplException(FileExceptionCode.FILE_UPLOAD_ERROR);
        }
    }
}
