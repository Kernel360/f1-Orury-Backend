package org.orury.domain.global.image;

import org.orury.common.util.S3Folder;

import java.util.List;

public interface ImageReader {
    String getImageLink(S3Folder domain, String profile);

    List<String> getImageLinks(S3Folder domain, List<String> images);
}
