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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
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
    private final AsyncTaskExecutor asyncTaskExecutor;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.default-image}")
    private String defaultImage;

    @Override
    public List<String> upload(S3Folder domain, List<MultipartFile> files) {
        // TODO 프론트 null check 로직 추가되면 return값 null로 바꿔야함
        if (ImageUtil.filesValidation(files)) return List.of();

        var fileNames = ImageUtil.createFileName(files.size());

        /**
         * 비동기 처리 : 파일 변환 & 이미지 업로드
         */
        asyncTaskExecutor.submit(() -> {
            var tempFiles = files.stream().map(this::convert).toList();
            upload(domain, tempFiles, fileNames);
        });

        // 이미지 imageStore.upload()를 기다리지 않고 fileNames 반환
        return fileNames;
    }

    @Override
    public String upload(S3Folder domain, MultipartFile file) {
        if (ImageUtil.fileValidation(file)) return null;
        return upload(domain, List.of(file)).get(0);
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

    private void upload(S3Folder domain, List<File> files, List<String> fileNames) {
        for (int idx = 0; idx < files.size(); idx++) {
            // S3에 파일들을 업로드
            amazonS3.putObject(new PutObjectRequest(bucket + domain.getName(), fileNames.get(idx), files.get(idx))
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            //임시 파일 삭제
            removeFile(files.get(idx));
        }
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

    private void removeFile(File file) {
        // 임시 파일을 삭제합니다.
        if (file.delete()) return;
        throw new FileException(FileExceptionCode.FILE_DELETE_ERROR);
    }
}
