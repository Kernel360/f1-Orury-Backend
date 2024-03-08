package org.orury.domain.global.listener;

import jakarta.persistence.Converter;
import org.orury.common.util.S3Folder;

@Converter
public class UserProfileConverter extends EntityImageConverter {
    @Override
    protected S3Folder domainName() {
        return S3Folder.USER;
    }
}
