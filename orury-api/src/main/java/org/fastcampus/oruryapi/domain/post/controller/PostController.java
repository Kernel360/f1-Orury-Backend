package org.fastcampus.oruryapi.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.fastcampus.oruryapi.domain.post.converter.request.PostCreateRequest;
import org.fastcampus.oruryapi.domain.post.converter.request.PostUpdateRequest;
import org.fastcampus.oruryapi.domain.post.converter.response.PostResponse;
import org.fastcampus.oruryapi.domain.post.converter.response.PostsWithCursorResponse;
import org.fastcampus.oruryapi.domain.post.service.PostService;
import org.fastcampus.oruryapi.domain.post.util.PostMessage;
import org.fastcampus.oruryapi.global.constants.NumberConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 생성", description = "게시글 정보를 받아, 게시글을 생성한다.")
    @PostMapping("/post")
    public ApiResponse<Object> createPost(@RequestBody PostCreateRequest request) {
        postService.createPost(request);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 id를 받아, 게시글 상세 정보를 돌려준다.")
    @GetMapping("/post/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long postId) {
        postService.addViewCount(postId);
        PostResponse response = postService.getPostById(postId);

        return ApiResponse.<PostResponse>builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_READ.getMessage())
                .data(response)
                .build();
    }

    @Operation(summary = "카테고리별 게시글 목록 조회", description = "카테고리(1: 자유게시판, 2: Q&A게시판)와 cursor값을 받아, '카테고리와 cursor값에 따른 다음 게시글 목록'과 'cursor값(목록의 마지막 게시글 id / 조회된 게시글 없다면 -1L)'을 돌려준다.")
    @GetMapping("/posts/{category}")
    public ApiResponse<PostsWithCursorResponse> getPostsByCategory(@PathVariable int category, @RequestParam Long cursor) {
        PostsWithCursorResponse responses = postService.getPostsByCategory(category, cursor, PageRequest.of(0, NumberConstants.PAGINATION_SIZE));

        return ApiResponse.<PostsWithCursorResponse>builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POSTS_READ.getMessage())
                .data(responses)
                .build();
    }

    @Operation(summary = "검색어에 따른 게시글 목록 조회", description = "검색어와 cursor값을 받아, '검색어와 cursor값에 따른 다음 게시글 목록'과 'cursor값(목록의 마지막 게시글 id / 조회된 게시글 없다면 -1L)'을 돌려준다.")
    @GetMapping("/posts")
    public ApiResponse<PostsWithCursorResponse> getPostsBySearchWord(@RequestParam String searchWord, Long cursor) {
        PostsWithCursorResponse responses = postService.getPostsBySearchWord(searchWord, cursor, PageRequest.of(0, NumberConstants.PAGINATION_SIZE));

        return ApiResponse.<PostsWithCursorResponse>builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POSTS_READ.getMessage())
                .data(responses)
                .build();
    }

    @Operation(summary = "게시글 수정", description = "게시글 정보를 받아, 게시글을 수정한다.")
    @PatchMapping("/post")
    public ApiResponse<Object> updatePost(@RequestBody PostUpdateRequest request) {
        postService.updatePost(request);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_UPDATED.getMessage())
                .build();
    }

    @Operation(summary = "게시글 삭제", description = "게시글 id를 받아, 게시글을 삭제한다.")
    @DeleteMapping("/post/{postId}")
    public ApiResponse<Object> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_DELETED.getMessage())
                .build();
    }
}