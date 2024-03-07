package org.orury.domain.post.infrastructure;

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
    @Value("${cloud.aws.s3.default-image}")
    private String DEFAULT_IMAGE;

    @Value("${cloud.aws.s3.url}")
    private String URL;

    /**
     * 추후에 유저, 크루의 기본 이미지 변경될 수 있어 나눠둠
     */
    @Override
    public String getImageLink(S3Folder domain, String profile) {
        if (S3Folder.USER == domain && StringUtils.isBlank(profile)) profile = DEFAULT_IMAGE;
        if (S3Folder.CREW == domain && StringUtils.isBlank(profile)) profile = DEFAULT_IMAGE;
        return getUrls(domain, List.of(profile)).get(0);
    }

    @Override
    public List<String> getImageLinks(S3Folder domain, List<String> images) {
        return getUrls(domain, images);
    }

    private List<String> getUrls(S3Folder domain, List<String> images) {
        try {
            return images.stream()
                    .map(it -> URL + domain.getName() + "/" + it)
                    .toList();
        } catch (Exception e) {
            throw new InfraImplException(FileExceptionCode.FILE_NOT_FOUND);
        }
    }
}
