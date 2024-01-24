package org.fastcampus.oruryclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurycommon.util.S3Repository;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
@RestController
public class ImageController {
    private final S3Repository s3Repository;

    @GetMapping
    public ApiResponse<ImageFormat> getImage(
            @RequestParam("image") String image
    ) {
        var urls = s3Repository.getUrls("test", image);
        var response = new ImageFormat(urls, List.of(image));

        return ApiResponse.<ImageFormat>builder()
                .status(200)
                .message("이미지 조회 성공")
                .data(response)
                .build();
    }

    @PostMapping
    public ApiResponse<ImageFormat> uploadImage(
            @RequestPart(required = false) MultipartFile[] images
    ) {
        List<String> imgs = s3Repository.upload("test", images);
        String image = ImageUrlConverter.convertListToString(imgs);
        var urls = s3Repository.getUrls("test", image);
        var response = new ImageFormat(urls, imgs);

        return ApiResponse.<ImageFormat>builder()
                .status(200)
                .message("이미지 업로드 성공")
                .data(response)
                .build();
    }

    @DeleteMapping
    public ApiResponse<ImageFormat> deleteImage(
            @RequestBody List<String> images
    ) {
        String image = ImageUrlConverter.convertListToString(images);
        var url = s3Repository.getUrls("test", image);
        log.error("url: {}", url);
        s3Repository.delete("test", url.toArray(new String[0]));
        var response = new ImageFormat(url, images);
        return ApiResponse.<ImageFormat>builder()
                .status(200)
                .message("이미지 삭제 성공")
                .data(response)
                .build();
    }
}
