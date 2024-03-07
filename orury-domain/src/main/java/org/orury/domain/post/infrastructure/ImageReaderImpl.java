package org.orury.domain.post.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.orury.common.error.code.FileExceptionCode;
import org.orury.common.error.exception.InfraImplException;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.image.ImageReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public String getImageLink(S3Folder domain, String profile) {
        if (S3Folder.USER == domain && StringUtils.isBlank(profile)) profile = defaultImage;
//        if (S3Folder.CREW==domain && StringUtils.isBlank(profile)) profile =  defaultImage;
        return getUrls(domain, List.of(profile)).get(0);
    }

    @Override
    public List<String> getImageLinks(S3Folder domain, List<String> images) {
        return getUrls(domain, images);
    }

    private List<String> getUrls(S3Folder domain, List<String> images) {
        try {
            return images.stream()
                    .map(it -> amazonS3.getUrl(bucket + domain.getName(), it).toString())
                    .toList();
        } catch (Exception e) {
            throw new InfraImplException(FileExceptionCode.FILE_NOT_FOUND);
        }
    }
}
