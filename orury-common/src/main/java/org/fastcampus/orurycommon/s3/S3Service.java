package org.fastcampus.orurycommon.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurycommon.error.code.FileExceptionCode;
import org.fastcampus.orurycommon.error.exception.FileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;
    private final Snowflake snowflake = new Snowflake(System.currentTimeMillis());
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(Long userId, MultipartFile multipartFile) {
        // 파일 이름을 랜덤하게 생성하여 중복을 방지합니다.
        long[] flakes = snowflake.parse(userId);

//        log.info("now: {}", now);
        Arrays.stream(flakes).forEach(it -> log.info("flake: {}", it));
        String fileName = "" + flakes[0] + flakes[1] + flakes[2];

        // 파일을 임시로 저장합니다.
        File file = convert(multipartFile);

        // S3에 파일을 업로드하고 URL을 반환합니다.
        String uploadUrl = putS3(file, fileName);

        // 임시 파일을 삭제합니다.
        removeFile(file);

        return uploadUrl;
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

    private String putS3(File file, String fileName) {
        // S3에 파일을 업로드하고 URL을 반환합니다.
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            log.error("FileException: {}", e.getMessage());
            throw new FileException(FileExceptionCode.FILE_UPLOAD_ERROR);
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeFile(File file) {
        // 임시 파일을 삭제합니다.
        if (file.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
        throw new FileException(FileExceptionCode.FILE_DELETE_ERROR);
    }
}