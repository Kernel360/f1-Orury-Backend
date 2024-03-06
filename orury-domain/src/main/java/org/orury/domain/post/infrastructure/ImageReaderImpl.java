package org.orury.domain.post.infrastructure;

import com.amazonaws.services.s3.AmazonS3;

import org.apache.commons.lang3.StringUtils;
import org.orury.common.util.ImageUtil;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.image.ImageReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageReaderImpl implements ImageReader {
    private final AmazonS3 amazonS3;

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
    public String getImageLink(S3Folder domain, String image) {
        if (StringUtils.isBlank(image)) return defaultImage; // 추후에 UserProfile이 아닌, 다른 디폴트 프로필 경로로 바꿔야함.
        return getUrls(domain, List.of(image)).get(0);
    }

    @Override
    public List<String> getImageLinks(S3Folder domain, List<String> images) {
        if (ImageUtil.imagesValidation(images)) return List.of();
        return getUrls(domain, images);
    }

    private List<String> getUrls(S3Folder domain, List<String> images) {
        return images.stream()
                .map(it -> amazonS3.getUrl(bucket + domain.getName(), it).toString())
                .toList();
    }
}
