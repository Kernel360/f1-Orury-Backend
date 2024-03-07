package org.orury.domain.post.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.orury.common.error.code.FileExceptionCode;
import org.orury.common.error.exception.FileException;
import org.orury.common.util.ImageUrlConverter;
import org.orury.common.util.ImageUtil;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.image.ImageStore;
import org.orury.domain.global.infrastructure.S3Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageStoreImpl implements ImageStore {
    private final AmazonS3 amazonS3;
    private final S3Repository s3Repository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.default-image}")
    private String defaultImage;

    @Override
    public List<String> upload(S3Folder domain, List<MultipartFile> multipartFiles) {
        if (ImageUtil.filesValidation(multipartFiles)) return null;
        // 파일들을 임시로 저장합니다.
        List<File> files = multipartFiles.stream().map(this::convert).toList();
        // S3에 파일들을 업로드
        var fileNames = putS3(domain, files);
        // 임시 파일들을 삭제합니다.
        files.forEach(this::removeFile);

        return fileNames;
    }

    @Override
    public String upload(S3Folder domain, MultipartFile profile) {
        if (ImageUtil.fileValidation(profile)) return null;
        return upload(domain, List.of(profile)).get(0);
    }

    @Override
    public void delete(S3Folder domain, String profile) {
        var image = ImageUtil.splitUrlToImage(profile);
        if (StringUtils.equals(image, defaultImage)) return;
        amazonS3.deleteObject(bucket + domain.getName(), image);
    }

    @Override
    public void delete(S3Folder domain, List<String> links) {
        if (ImageUtil.imagesValidation(links)) return;
        links.stream()
                .map(ImageUrlConverter::splitUrlToImage)
                .forEach(it -> amazonS3.deleteObject(bucket + domain.getName(), it));
    }

    @Override
    public void oldS3ImagesDelete(String domain, List<String> images) {
        if (images == null || images.isEmpty()) return;
        s3Repository.delete(domain, images);
    }

    private File convert(MultipartFile multipartFile) {
        // MultipartFile을 File로 변환합니다.
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new FileException(FileExceptionCode.FILE_NOT_FOUND);
        }
        return file;
    }

    private List<String> putS3(S3Folder domain, List<File> files) {
        // S3에 파일들을 업로드
        return files.stream()
                .map(it -> requestPutObject(bucket + domain.getName(), it))
                .toList();
    }

    private void removeFile(File file) {
        // 임시 파일을 삭제합니다.
        if (file.delete()) return;
        throw new FileException(FileExceptionCode.FILE_DELETE_ERROR);
    }

    private String requestPutObject(String folder, File file) {
        try {
            String fileName = ImageUtil.createFileName();
            amazonS3.putObject(new PutObjectRequest(folder, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
            return fileName;
        } catch (Exception e) {
            throw new FileException(FileExceptionCode.FILE_UPLOAD_ERROR);
        }
    }
}
