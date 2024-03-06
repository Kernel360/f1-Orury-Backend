package org.orury.domain.global.image;

import org.orury.common.util.S3Folder;

import java.util.List;

public interface ImageReader {
    String getUserImageLink(String profile);

    String getImageLink(S3Folder domain, String image);

    List<String> getImageLinks(S3Folder domain, List<String> images);
}
