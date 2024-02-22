package org.orury.common.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.FileExceptionCode;
import org.orury.common.error.exception.FileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Repository {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Getter
    @Value("${cloud.aws.s3.default-image}")
    private String defaultImage;

    public List<String> getUrls(String domain, List<String> images) {
        return images.stream()
                .map(it -> amazonS3.getUrl(bucket + domain, it).toString())
                .toList();
    }

    public List<String> upload(String domain, List<MultipartFile> multipartFiles) {
        // 파일들을 임시로 저장합니다.
        List<File> files = multipartFiles.stream()
                .map(this::convert)
                .toList();

        // S3에 파일들을 업로드하고 Name을 반환합니다.
        List<String> fileNames = putS3(domain, files);

        // 임시 파일들을 삭제합니다.
        files.forEach(this::removeFile);

        return fileNames;
    }

    //DB에 있는 이미지를 삭제하는 메서드
    public void delete(String domain, List<String> urls) {
        urls.stream()
                .map(ImageUrlConverter::splitUrlToImage)
                .forEach(it -> {
                    //유저 기본 프로필 이미지인 경우 삭제하지 않습니다.
                    if (domain.equals(S3Folder.USER.getName()) && it.equals(defaultImage)) return;
                    // 해당 버킷 경로의 S3에 파일을 삭제합니다.
                    amazonS3.deleteObject(bucket + domain, it);
                });
    }

    private File convert(MultipartFile multipartFile) {
        // MultipartFile을 File로 변환합니다.
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            log.error("FileException: {}", e.getMessage());
            throw new FileException(FileExceptionCode.FILE_NOT_FOUND);
        }
        return file;
    }

    private List<String> putS3(String domain, List<File> files) {
        // S3에 파일을 업로드하고 이미지 key값을 받환합니다.
        return files.stream()
                .map(it -> requestPutObject(bucket + domain, it))
                .toList();
    }

    private void removeFile(File file) {
        // 임시 파일을 삭제합니다.
        if (file.delete()) {
            log.info("File delete success");
            return;
        }
        log.error("File delete fail");
        throw new FileException(FileExceptionCode.FILE_DELETE_ERROR);
    }

    private String requestPutObject(String folder, File file) {
        // 파일 이름을 랜덤하게 생성하여 중복을 방지합니다.
        String fileName = UUID.randomUUID().toString().substring(0, 15);
        try {
            amazonS3.putObject(new PutObjectRequest(folder, fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return fileName;
        } catch (Exception e) {
            log.error("FileException: {}", e.getMessage());
            throw new FileException(FileExceptionCode.FILE_UPLOAD_ERROR);
        }
    }
}