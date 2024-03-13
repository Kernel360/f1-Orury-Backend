package org.orury.domain.global.image;

import org.orury.common.util.S3Folder;

import java.io.File;
import java.util.List;

public interface ImageStore {
    void upload(S3Folder domain, List<File> files, List<String> filesName);

    void delete(S3Folder domain, List<String> imageLinks);

    void delete(S3Folder domain, String profile);
}
