package org.orury.domain.image.domain;

import org.orury.common.util.S3Folder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageStore {
    List<String> upload(S3Folder domain, List<MultipartFile> files);

    String upload(S3Folder domain, MultipartFile file);

    void delete(S3Folder domain, List<String> imageLinks);

    void delete(S3Folder domain, String profile);
}
