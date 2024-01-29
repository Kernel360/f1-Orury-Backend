package org.fastcampus.oruryclient.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.post.converter.message.PostMessage;
import org.fastcampus.oruryclient.post.converter.request.PostCreateRequest;
import org.fastcampus.oruryclient.post.converter.request.PostUpdateRequest;
import org.fastcampus.oruryclient.post.converter.response.PostResponse;
import org.fastcampus.oruryclient.post.converter.response.PostsResponse;
import org.fastcampus.oruryclient.post.converter.response.PostsWithCursorResponse;
import org.fastcampus.oruryclient.post.converter.response.PostsWithPageResponse;
import org.fastcampus.oruryclient.post.service.PostLikeService;
import org.fastcampus.oruryclient.post.service.PostService;
import org.fastcampus.oruryclient.user.service.UserService;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.fastcampus.orurydomain.user.dto.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 생성", description = "게시글 정보를 받아, 게시글을 생성한다.")
    @PostMapping
    public ApiResponse<Object> createPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart PostCreateRequest request,
            @RequestPart(required = false) MultipartFile... image
    ) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        PostDto postDto = request.toDto(userDto);

        postService.createPost(postDto, image);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 id를 받아, 게시글 상세 정보를 돌려준다.")
    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PostDto postDto = postService.getPostDtoById(id);
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        postService.addViewCount(postDto);

        boolean isLike = postLikeService.isLiked(userPrincipal.id(), id);
        PostResponse response = PostResponse.of(postDto, userDto, isLike);

        return ApiResponse.<PostResponse>builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_READ.getMessage())
                .data(response)
                .build();
    }

    @Operation(summary = "카테고리별 게시글 목록 조회", description = "카테고리(1: 자유게시판, 2: Q&A게시판)와 cursor값을 받아, '카테고리와 cursor값에 따른 다음 게시글 목록'과 'cursor값(목록의 마지막 게시글 id / 조회된 게시글 없다면 -1L)'을 돌려준다.")
    @GetMapping("/category/{category}")
    public ApiResponse<PostsWithCursorResponse> getPostsByCategory(@PathVariable int category, @RequestParam Long cursor) {
        List<PostDto> postDtos = postService.getPostDtosByCategory(category, cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));
        List<PostsResponse> postsResponses = postDtos.stream()
                .map(PostsResponse::of)
                .toList();

        PostsWithCursorResponse response = PostsWithCursorResponse.of(postsResponses, cursor);

        return ApiResponse.<PostsWithCursorResponse>builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POSTS_READ.getMessage())
                .data(response)
                .build();
    }

    @Operation(summary = "검색어에 따른 게시글 목록 조회", description = "검색어와 cursor값을 받아, '검색어와 cursor값에 따른 다음 게시글 목록'과 'cursor값(목록의 마지막 게시글 id / 조회된 게시글 없다면 -1L)'을 돌려준다.")
    @GetMapping
    public ApiResponse<PostsWithCursorResponse> getPostsBySearchWord(@RequestParam String searchWord, @RequestParam Long cursor) {
        List<PostDto> postDtos = postService.getPostDtosBySearchWord(searchWord, cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));
        List<PostsResponse> postsResponses = postDtos.stream()
                .map(PostsResponse::of)
                .toList();

        PostsWithCursorResponse response = PostsWithCursorResponse.of(postsResponses, cursor);

        return ApiResponse.<PostsWithCursorResponse>builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POSTS_READ.getMessage())
                .data(response)
                .build();
    }

    @Operation(summary = "인기 게시글 목록 조회", description = "page값을 받아, 'page번호에 따른 인기 게시글 목록'과 'page값(다음으로 조회할 page 번호 / 현재 마지막 페이지를 반환한다면 -1)'을 돌려준다.")
    @GetMapping("/hot")
    public ApiResponse<PostsWithPageResponse> getHotPosts(@RequestParam int page) {
        Page<PostDto> postDtos = postService.getHotPostDtos(PageRequest.of(page, NumberConstants.POST_PAGINATION_SIZE));
        List<PostsResponse> postsResponses = postDtos.stream()
                .map(PostsResponse::of)
                .toList();

        int nextPage = postService.getNextPage(postDtos, page);
        PostsWithPageResponse response = PostsWithPageResponse.of(postsResponses, nextPage);

        return ApiResponse.<PostsWithPageResponse>builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POSTS_READ.getMessage())
                .data(response)
                .build();
    }

    @Operation(summary = "게시글 수정", description = "게시글 정보를 받아, 게시글을 수정한다.")
    @PatchMapping
    public ApiResponse<Object> updatePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart PostUpdateRequest request,
            @RequestPart(required = false) MultipartFile... image
    ) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        PostDto postDto = postService.getPostDtoById(request.id());
        postService.isValidate(postDto, userDto);

        PostDto updatingPostDto = request.toDto(postDto, userDto);

        postService.updatePost(updatingPostDto, image);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_UPDATED.getMessage())
                .build();
    }

    @Operation(summary = "게시글 삭제", description = "게시글 id를 받아, 게시글을 삭제한다.")
    @DeleteMapping("/{id}")
    public ApiResponse<Object> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        PostDto postDto = postService.getPostDtoById(id);
        postService.isValidate(postDto, userDto);

        postService.deletePost(postDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_DELETED.getMessage())
                .build();
    }
}