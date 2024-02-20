package org.oruryclient.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oruryclient.post.converter.message.PostMessage;
import org.oruryclient.post.service.PostLikeService;
import org.orurydomain.base.converter.ApiResponse;
import org.orurydomain.post.db.model.PostLike;
import org.orurydomain.post.db.model.PostLikePK;
import org.orurydomain.post.dto.PostLikeDto;
import org.orurydomain.user.dto.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/like")
@RestController
public class PostLikeController {
    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 좋아요 생성", description = "게시글 id를 받아, 게시글 좋아요를 생성한다.")
    @PostMapping("/{postId}")
    public ApiResponse<Object> createPostLike(@PathVariable Long postId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PostLikeDto postLikeDto = PostLikeDto.from(PostLike.of(PostLikePK.of(userPrincipal.id(), postId)));

        postLikeService.createPostLike(postLikeDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_LIKE_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "게시글 좋아요 삭제", description = "게시글 id를 받아, 게시글 좋아요를 삭제한다.")
    @DeleteMapping("/{postId}")
    public ApiResponse<Object> deletePostLike(@PathVariable Long postId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PostLikeDto postLikeDto = PostLikeDto.from(PostLike.of(PostLikePK.of(userPrincipal.id(), postId)));

        postLikeService.deletePostLike(postLikeDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_LIKE_DELETED.getMessage())
                .build();
    }
}
