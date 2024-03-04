package org.orury.domain.post.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.orury.common.util.ImageUtil;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.image.ImageReader;
import org.orury.domain.global.infrastructure.S3Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageReaderImpl implements ImageReader {
    private final AmazonS3 amazonS3;
    private final S3Repository s3Repository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.default-image}")
    private String defaultImage;

    @Override
    public String getUserImageLink(String profile) {
        if (StringUtils.isBlank(profile)) return defaultImage;
        return getUrls(S3Folder.USER, List.of(profile)).get(0);
    }

    @Override
    public List<String> getImageLinks(S3Folder domain, List<String> images) {
        if (ImageUtil.imagesValidation(images)) return List.of();
        return getUrls(domain, images);
    }

    @Override
    public List<String> getUrls(String domain, List<String> image) {
        if (image == null || image.isEmpty()) return List.of();
        return s3Repository.getUrls(domain, image);
    }

    @Override
    public String getUserImageUrl(String image) {
        return s3Repository.getUrls(S3Folder.USER.getName(), List.of(image)).get(0);
    }

    private List<String> getUrls(S3Folder domain, List<String> images) {
        return images.stream()
                .map(it -> amazonS3.getUrl(bucket + domain.getName(), it).toString())
                .toList();
    }
}
