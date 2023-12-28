package org.fastcampus.oruryclient.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.domain.base.converter.ApiResponse;
import org.fastcampus.oruryclient.domain.post.converter.dto.PostLikeDto;
import org.fastcampus.oruryclient.domain.post.converter.request.PostLikeRequest;
import org.fastcampus.oruryclient.domain.post.db.model.PostLike;
import org.fastcampus.oruryclient.domain.post.db.model.PostLikePK;
import org.fastcampus.oruryclient.domain.post.service.PostLikeService;
import org.fastcampus.oruryclient.domain.post.util.PostMessage;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class PostLikeController {
    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 좋아요 생성", description = "유저 id와 게시글 id를 받아, 게시글 좋아요를 생성한다.")
    @PutMapping("/post/like")
    public ApiResponse<Object> createPostLike(@RequestBody PostLikeRequest postLikeRequest) {
        PostLikeDto postLikeDto = PostLikeDto.from(PostLike.of(PostLikePK.of(NumberConstants.USER_ID, postLikeRequest.postId())));

        postLikeService.createPostLike(postLikeDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_LIKE_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "게시글 좋아요 삭제", description = "유저 id와 게시글 id를 받아, 게시글 좋아요를 삭제한다.")
    @DeleteMapping("/post/like")
    public ApiResponse<Object> deletePostLike(@RequestBody PostLikeRequest postLikeRequest) {
        PostLikeDto postLikeDto = PostLikeDto.from(PostLike.of(PostLikePK.of(NumberConstants.USER_ID, postLikeRequest.postId())));

        postLikeService.deletePostLike(postLikeDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_LIKE_DELETED.getMessage())
                .build();
    }
}
