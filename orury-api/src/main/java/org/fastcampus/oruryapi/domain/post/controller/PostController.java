package org.fastcampus.oruryapi.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.fastcampus.oruryapi.domain.post.converter.request.PostCreateRequest;
import org.fastcampus.oruryapi.domain.post.converter.request.PostUpdateRequest;
import org.fastcampus.oruryapi.domain.post.converter.response.PostResponse;
import org.fastcampus.oruryapi.domain.post.converter.response.PostsWithCursorResponse;
import org.fastcampus.oruryapi.domain.post.service.PostService;
import org.fastcampus.oruryapi.global.message.info.InfoMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public ApiResponse<Object> createPost(@RequestBody PostCreateRequest request) {
        postService.createPost(request);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.POST_CREATED.getMessage())
                .build();
    }

    @GetMapping("/post/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long postId) {
        postService.addViewCount(postId);
        PostResponse response = postService.getPostById(postId);

        return ApiResponse.<PostResponse>builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.POST_READ.getMessage())
                .data(response)
                .build();
    }

    @GetMapping("/posts/{category}")
    public ApiResponse<PostsWithCursorResponse> getPostsByCategory(@PathVariable int category, @RequestParam Long cursor) {
        PostsWithCursorResponse responses = postService.getPostsByCategory(category, cursor, PageRequest.of(0, 10));

        return ApiResponse.<PostsWithCursorResponse>builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.POSTS_READ.getMessage())
                .data(responses)
                .build();
    }

    @GetMapping("/posts")
    public ApiResponse<PostsWithCursorResponse> getPostsBySearchWord(@RequestParam String searchWord, Long cursor) {
        PostsWithCursorResponse responses = postService.getPostsBySearchWord(searchWord, cursor, PageRequest.of(0, 10));

        return ApiResponse.<PostsWithCursorResponse>builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.POSTS_READ.getMessage())
                .data(responses)
                .build();
    }

    @PatchMapping("/post")
    public ApiResponse<Object> updatePost(@RequestBody PostUpdateRequest request) {
        postService.updatePost(request);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.POST_UPDATED.getMessage())
                .build();
    }

    @DeleteMapping("/post/{postId}")
    public ApiResponse<Object> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.COMMENT_DELETED.getMessage())
                .build();
    }
}