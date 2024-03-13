package org.orury.client.global.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.FileExceptionCode;
import org.orury.common.error.exception.FileException;
import org.orury.common.util.ImageUtil;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.image.ImageStore;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class ImageAsyncStore {
    private final ImageStore imageStore;

    public List<String> upload(S3Folder domain, List<MultipartFile> files) {
        // TODO 프론트 null check 로직 추가되면 return값 null로 바꿔야함
        if (ImageUtil.filesValidation(files)) return List.of();

        var tempFiles = files.stream().map(this::convert).toList();
        var fileNames = ImageUtil.createFileName(files.size());
        imageStore.upload(domain, tempFiles, fileNames);

        // 이미지 imageStore.upload()를 기다리지 않고 fileNames 반환
        return fileNames;
    }

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
}
