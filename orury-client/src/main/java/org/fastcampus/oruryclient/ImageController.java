package org.fastcampus.oruryclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurycommon.s3.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final S3Service s3Service;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile multipartFile) throws IOException {
        // S3 서비스를 이용하여 이미지를 업로드하고 URL을 받습니다.
        String uploadUrl = s3Service.upload(userId, multipartFile);

        // URL을 응답으로 반환합니다.
        return ResponseEntity.ok(uploadUrl);
    }
}
