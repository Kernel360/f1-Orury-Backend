package org.orury.domain.global.listener;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.orury.common.config.BeanUtils;
import org.orury.common.util.ImageUtil;
import org.orury.common.util.S3Folder;
import org.orury.domain.image.domain.ImageReader;

@Converter
public abstract class EntityImageConverter implements AttributeConverter<String, String> {
    abstract protected S3Folder domainName();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return ImageUtil.splitUrlToImage(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        var imageReader = BeanUtils.getBean(ImageReader.class);
        return imageReader.getImageLink(domainName(), dbData);
    }
}
