package org.orury.domain.global.listener;

import org.orury.common.util.S3Folder;

public class ReviewImagesConverter extends EntityImagesConverter {
    @Override
    protected S3Folder domainName() {
        return S3Folder.REVIEW;
    }
}
