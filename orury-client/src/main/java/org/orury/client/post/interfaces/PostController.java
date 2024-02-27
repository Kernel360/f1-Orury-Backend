package org.orury.client.post.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.post.application.PostFacade;
import org.orury.client.post.interfaces.request.PostRequest;
import org.orury.client.post.interfaces.response.PostsWithCursorResponse;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.user.dto.UserPrincipal;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.orury.client.post.interfaces.message.PostMessage.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
@RestController
public class PostController {
    private final PostFacade postFacade;

    @Operation(summary = "게시글 생성", description = "게시글 정보를 받아, 게시글을 생성한다.")
    @PostMapping
    public ApiResponse createPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart PostRequest request,
            @RequestPart(required = false) List<MultipartFile> image
    ) {
        postFacade.createPost(userPrincipal.id(), request, image);
        return ApiResponse.of(POST_CREATED.getMessage());
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 id를 받아, 게시글 상세 정보를 돌려준다.")
    @GetMapping("/{id}")
    public ApiResponse getPostById(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        var response = postFacade.getPostById(userPrincipal.id(), id);
        return ApiResponse.of(POST_READ.getMessage(), response);
    }

    @Operation(summary = "카테고리별 게시글 목록 조회", description = "카테고리(1: 자유게시판, 2: Q&A게시판)와 cursor값을 받아, '카테고리와 cursor값에 따른 다음 게시글 목록'과 'cursor값(목록의 마지막 게시글 id / 조회된 게시글 없다면 -1L)'을 돌려준다.")
    @GetMapping("/category/{category}")
    public ApiResponse getPostsByCategory(@PathVariable int category, @RequestParam Long cursor) {
        var posts = postFacade.getPostsByCategory(category, cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));
        var response = PostsWithCursorResponse.of(posts, cursor);
        return ApiResponse.of(POSTS_READ.getMessage(), response);
    }

    @Operation(summary = "검색어에 따른 게시글 목록 조회", description = "검색어와 cursor값을 받아, '검색어와 cursor값에 따른 다음 게시글 목록'과 'cursor값(목록의 마지막 게시글 id / 조회된 게시글 없다면 -1L)'을 돌려준다.")
    @GetMapping
    public ApiResponse getPostsBySearchWord(@RequestParam String searchWord, @RequestParam Long cursor) {
        var posts = postFacade.getPostsBySearchWord(searchWord, cursor);
        PostsWithCursorResponse response = PostsWithCursorResponse.of(posts, cursor);
        return ApiResponse.of(POSTS_READ.getMessage(), response);
    }

    @Operation(summary = "인기 게시글 목록 조회", description = "page값을 받아, 'page번호에 따른 인기 게시글 목록'과 'page값(다음으로 조회할 page 번호 / 현재 마지막 페이지를 반환한다면 -1)'을 돌려준다.")
    @GetMapping("/hot")
    public ApiResponse getHotPosts(@RequestParam int page) {
        var response = postFacade.getHotPosts(page);
        return ApiResponse.of(POSTS_READ.getMessage(), response);
    }

    @Operation(summary = "게시글 수정", description = "게시글 정보를 받아, 게시글을 수정한다.")
    @PatchMapping
    public ApiResponse updatePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart PostRequest request,
            @RequestPart(required = false) List<MultipartFile> image
    ) {
        postFacade.updatePost(userPrincipal.id(), request, image);
        return ApiResponse.of(POST_UPDATED.getMessage());
    }

    @Operation(summary = "게시글 삭제", description = "게시글 id를 받아, 게시글을 삭제한다.")
    @DeleteMapping("/{id}")
    public ApiResponse deletePost(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        postFacade.deletePost(userPrincipal.id(), id);
        return ApiResponse.of(POST_DELETED.getMessage());
    }

    @Operation(summary = "게시글 좋아요 생성", description = "게시글 id를 받아, 게시글 좋아요를 생성한다.")
    @PostMapping("/like/{postId}")
    public ApiResponse createPostLike(@PathVariable Long postId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        postFacade.createPostLike(userPrincipal.id(), postId);
        return ApiResponse.of(POST_LIKE_CREATED.getMessage());
    }

    @Operation(summary = "게시글 좋아요 삭제", description = "게시글 id를 받아, 게시글 좋아요를 삭제한다.")
    @DeleteMapping("/like/{postId}")
    public ApiResponse deletePostLike(@PathVariable Long postId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        postFacade.deletePostLike(userPrincipal.id(), postId);
        return ApiResponse.of(POST_LIKE_DELETED.getMessage());
    }
}