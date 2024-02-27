package org.orury.domain.global.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.infrastructure.S3Repository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageUtils {
    private final S3Repository s3Repository;

    public String getUserDefaultImage() {
        return s3Repository.getDefaultImage();
    }

    public String getUserImageUrl(String image) {
        return s3Repository.getUrls(S3Folder.USER.getName(), List.of(image)).get(0);
    }

    public List<String> getUrls(String domain, List<String> image) {
        if (image == null || image.isEmpty()) return List.of();
        return s3Repository.getUrls(domain, image);
    }

    public void oldS3ImagesDelete(String domain, List<String> images) {
        if (images == null || images.isEmpty()) return;
        s3Repository.delete(domain, images);
    }

    public void oldS3ImagesDelete(String domain, String image) {
        if (image == null || image.isEmpty()) return;
        s3Repository.delete(domain, List.of(image));
    }

    public List<String> upload(String domain, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) return List.of();
        return s3Repository.upload(domain, images);
    }

    public String upload(String domain, MultipartFile image) {
        return s3Repository.upload(domain, List.of(image)).get(0);
    }
}
