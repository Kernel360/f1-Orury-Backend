package org.orury.client.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.post.converter.message.PostMessage;
import org.orury.client.post.service.PostLikeService;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.post.db.model.PostLike;
import org.orury.domain.post.db.model.PostLikePK;
import org.orury.domain.post.dto.PostLikeDto;
import org.orury.domain.user.dto.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts/like")
@RestController
public class PostLikeController {
    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 좋아요 생성", description = "게시글 id를 받아, 게시글 좋아요를 생성한다.")
    @PostMapping("/{postId}")
    public ApiResponse createPostLike(@PathVariable Long postId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PostLikeDto postLikeDto = PostLikeDto.from(PostLike.of(PostLikePK.of(userPrincipal.id(), postId)));

        postLikeService.createPostLike(postLikeDto);

        return ApiResponse.of(PostMessage.POST_LIKE_CREATED.getMessage());
    }

    @Operation(summary = "게시글 좋아요 삭제", description = "게시글 id를 받아, 게시글 좋아요를 삭제한다.")
    @DeleteMapping("/{postId}")
    public ApiResponse deletePostLike(@PathVariable Long postId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PostLikeDto postLikeDto = PostLikeDto.from(PostLike.of(PostLikePK.of(userPrincipal.id(), postId)));

        postLikeService.deletePostLike(postLikeDto);

        return ApiResponse.of(PostMessage.POST_LIKE_DELETED.getMessage());
    }
}
