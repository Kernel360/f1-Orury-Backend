package org.fastcampus.oruryclient.post.controller;

import org.fastcampus.oruryclient.config.ControllerTest;
import org.fastcampus.oruryclient.config.WithUserPrincipal;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.post.converter.message.PostMessage;
import org.fastcampus.oruryclient.post.converter.request.PostCreateRequest;
import org.fastcampus.oruryclient.post.converter.request.PostUpdateRequest;
import org.fastcampus.oruryclient.post.converter.response.PostsResponse;
import org.fastcampus.oruryclient.post.converter.response.PostsWithCursorResponse;
import org.fastcampus.oruryclient.post.converter.response.PostsWithPageResponse;
import org.fastcampus.orurycommon.error.code.PostErrorCode;
import org.fastcampus.orurycommon.error.code.UserErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Disabled
@DisplayName("[Controller] 게시글 관련 테스트")
@WithUserPrincipal
class PostControllerTest extends ControllerTest {

    @DisplayName("[GET] 게시글 id로 게시글 상세 조회 - 성공")
    @Test
    void when_PostId_Then_PostDetailSuccessfully() throws Exception {
        //given
        Long postId = 1L;
        Long userId = NumberConstants.USER_ID;
        UserDto userDto = createUserDto(userId);
        boolean isLike = true;
        PostDto postDto = createPostDto(postId);

        given(postService.getPostDtoById(postId)).willReturn(postDto);
        given(userService.getUserDtoById(userId)).willReturn(userDto);
        given(postLikeService.isLiked(userId, postId)).willReturn(isLike);

        //when
        mvc.perform(get("/api/v1/posts/" + postId).accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(postId))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.content").value("content"))
                .andExpect(jsonPath("$.data.is_like").value(true));

        //then
        then(postService).should()
                .getPostDtoById(postId);
        then(userService).should()
                .getUserDtoById(userId);
        then(postService).should()
                .addViewCount(postDto);
        then(postLikeService).should()
                .isLiked(userId, postId);
    }

    @DisplayName("[GET] 게시글 id로 게시글 상세 조회 - 실패 (존재하지 않는 게시글)")
    @Test
    void when_PostId_Then_PostDetailFailed() throws Exception {
        //given
        Long postId = 1L;
        Long userId = NumberConstants.USER_ID;
        PostErrorCode code = PostErrorCode.NOT_FOUND;
        given(postService.getPostDtoById(postId)).willThrow(new BusinessException(code));

        //when
        mvc.perform(get("/api/v1/posts/" + postId).accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        //then
        then(postService).should()
                .getPostDtoById(postId);
        then(userService).should(never())
                .getUserDtoById(userId);
        then(postService).should(never())
                .addViewCount(any());
        then(postLikeService).should(never())
                .isLiked(any(), any());

    }

    @DisplayName("[POST] 유저 정보와 게시글 정보를 받아 게시글 생성시 예외 처리 - 성공 (이미지 없는 경우)")
    @Test
    void when_RequestDoesNotContainsImage_Then_CreatePostSuccessfully() throws Exception {
        //given
        Long userId = NumberConstants.USER_ID;
        UserDto userDto = createUserDto(userId);
        PostCreateRequest postCreateRequest = createPostCreateRequest();

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "testPostCreateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(postCreateRequest).getBytes(StandardCharsets.UTF_8)
        );

        PostMessage message = PostMessage.POST_CREATED;

        given(userService.getUserDtoById(userId)).willReturn(userDto);

        //when
        mvc.perform(multipart(POST, "/api/v1/posts")
                        .file(request)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.getMessage()))
        ;

        //then
        then(userService).should()
                .getUserDtoById(userId);
        then(postService).should()
                .createPost(any(), any());
    }

    @DisplayName("[POST] 유저 정보와 게시글 정보를 받아 게시글 생성시 예외 처리 - 성공 (이미지 있는 경우)")
    @Test
    void when_RequestContainsImage_Then_CreatePostSuccessfully() throws Exception {
        //given
        Long userId = NumberConstants.USER_ID;
        UserDto userDto = createUserDto(userId);
        PostCreateRequest postCreateRequest = createPostCreateRequest();

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "testPostCreateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(postCreateRequest).getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "testImageFile",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        PostMessage message = PostMessage.POST_CREATED;

        given(userService.getUserDtoById(userId)).willReturn(userDto);

        //when
        mvc.perform(multipart(POST, "/api/v1/posts")
                        .file(request)
                        .file(image)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.getMessage()))
        ;

        //then
        then(userService).should()
                .getUserDtoById(userId);
        then(postService).should()
                .createPost(any(), any());
    }

    @DisplayName("[POST] 권한 없는 유저 정보와 게시글 정보를 받아 게시글 생성 - 실패")
    @Test
    void when_InvalidUserAndCreatePost_Then_ThrowBusinessException() throws Exception {
        //given
        Long userId = NumberConstants.USER_ID;
        PostCreateRequest postCreateRequest = createPostCreateRequest();

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "testPostCreateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(postCreateRequest).getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "testImageFile",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        UserErrorCode code = UserErrorCode.NOT_FOUND;

        given(userService.getUserDtoById(userId))
                .willThrow(new BusinessException(code));

        //when
        mvc.perform(multipart(POST, "/api/v1/posts")
                        .file(request)
                        .file(image)
                        .with(csrf()))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        //then
        then(userService).should()
                .getUserDtoById(userId);
        then(postService).should(never())
                .createPost(any());
    }

    @DisplayName("[PATCH] 게시글 정보를 받아 게시글을 수정 - 성공 (이미지 있는 경우)")
    @Test
    void when_RequestContainsImage_Then_UpdatePostSuccessfully() throws Exception {
        //given
        UserDto userDto = createUserDto();
        Long postId = 1L;
        PostDto postDto = createPostDto(postId);
        PostUpdateRequest postUpdateRequest = createPostUpdateRequest();

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "testPostUpdateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(postUpdateRequest).getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile image1 = new MockMultipartFile(
                "file",
                "testImageFile1",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        MockMultipartFile image2 = new MockMultipartFile(
                "file",
                "testImageFile2",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!!".getBytes()
        );

        PostMessage message = PostMessage.POST_UPDATED;

        given(userService.getUserDtoById(NumberConstants.USER_ID))
                .willReturn(userDto);
        given(postService.getPostDtoById(postId))
                .willReturn(postDto);

        //when
        mvc.perform(multipart(PATCH, "/api/v1/posts")
                        .file(request)
                        .file(image1)
                        .file(image2)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.getMessage()))
        ;

        //then
        then(userService).should()
                .getUserDtoById(any());
        then(postService).should()
                .getPostDtoById(any());
        then(postService).should()
                .updatePost(any(), any());
    }

    @DisplayName("[PATCH] 게시글 정보를 받아 게시글을 수정 - 성공 (이미지 없는 경우)")
    @Test
    void when_RequestDoesNotContainsImage_Then_UpdatePostSuccessfully() throws Exception {
        //given
        UserDto userDto = createUserDto();
        Long postId = 1L;
        PostDto postDto = createPostDto(postId);
        PostUpdateRequest postUpdateRequest = createPostUpdateRequest();

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "testPostUpdateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(postUpdateRequest).getBytes(StandardCharsets.UTF_8)
        );

        PostMessage message = PostMessage.POST_UPDATED;

        given(userService.getUserDtoById(NumberConstants.USER_ID))
                .willReturn(userDto);
        given(postService.getPostDtoById(postId))
                .willReturn(postDto);

        //when
        mvc.perform(multipart(PATCH, "/api/v1/posts")
                        .file(request)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.getMessage()))
        ;

        //then
        then(userService).should()
                .getUserDtoById(any());
        then(postService).should()
                .getPostDtoById(any());
        then(postService).should()
                .updatePost(any(), any());
    }

    @DisplayName("[PATCH] 없는 게시글 아이디를 가지고 게시글 수정할 경우 예외 처리  - 실패")
    @Test
    void given_InvalidPostId_When_UpdatePost_Then_NotFoundException() throws Exception {
        //given
        UserDto userDto = createUserDto();
        Long postId = 1L;
        PostDto postDto = createPostDto(postId);
        PostUpdateRequest postUpdateRequest = createPostUpdateRequest();

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "testPostUpdateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(postUpdateRequest).getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "testImageFile",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        PostErrorCode code = PostErrorCode.NOT_FOUND;

        given(userService.getUserDtoById(NumberConstants.USER_ID))
                .willReturn(userDto);
        given(postService.getPostDtoById(postId))
                .willThrow(new BusinessException(code));

        //when
        mvc.perform(multipart(PATCH, "/api/v1/posts")
                        .file(request)
                        .file(image)
                        .with(csrf()))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        //then
        then(userService).should()
                .getUserDtoById(any());
        then(postService).should()
                .getPostDtoById(any());
        then(postService).should(never())
                .updatePost(any(), any());
    }

    @DisplayName("[PATCH] 게시글 수정 권한이 없는 유저가 게시글 수정 요청할 경우 예외 처리  - 실패")
    @Test
    void given_NotAuthorizationUserRequest_When_UpdatedPost_Then_ForbiddenException() throws Exception {
        //given
        Long postId = 1L;
        PostDto postDto = createPostDto(postId, 99L);
        UserDto userDto = createUserDto();
        PostUpdateRequest postUpdateRequest = createPostUpdateRequest();

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "testPostUpdateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(postUpdateRequest).getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "testImageFile",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        PostErrorCode code = PostErrorCode.FORBIDDEN;

        given(userService.getUserDtoById(NumberConstants.USER_ID))
                .willReturn(userDto);
        given(postService.getPostDtoById(1L))
                .willReturn(postDto);
        willThrow(new BusinessException(code)).given(postService)
                .isValidate(postDto, userDto);

        //when
        mvc.perform(multipart(PATCH, "/api/v1/posts")
                        .file(request)
                        .file(image)
                        .with(csrf()))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        //then
        then(userService).should()
                .getUserDtoById(any());
        then(postService).should()
                .getPostDtoById(any());
        then(postService).should(never())
                .updatePost(any(), any());
    }

    @DisplayName("[DELETE] 게시글 id를 받아 게시글을 삭제  - 성공")
    @Test
    void given_PostId_When_DeletePostSuccessFully() throws Exception {
        //given
        Long postId = 1L;
        UserDto userDto = createUserDto();
        PostDto postDto = createPostDto(postId);
        PostMessage code = PostMessage.POST_DELETED;

        given(userService.getUserDtoById(NumberConstants.USER_ID))
                .willReturn(userDto);
        given(postService.getPostDtoById(postId))
                .willReturn(postDto);

        //when
        mvc.perform(delete("/api/v1/posts/" + postId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        //then
        then(userService).should()
                .getUserDtoById(any());
        then(postService).should()
                .getPostDtoById(any());
        then(postService).should()
                .isValidate(any(), any());
        then(postService).should()
                .deletePost(any());
    }

    @DisplayName("[DELETE] 없는 게시글 id를 받아 게시글을 삭제 요청시 예외 처리 - 실패")
    @Test
    void given_InvalidPostId_When_DeletePost_Then_NotFoundException() throws Exception {
        //given
        Long postId = 1L;
        UserDto userDto = createUserDto();
        PostErrorCode code = PostErrorCode.NOT_FOUND;

        given(userService.getUserDtoById(NumberConstants.USER_ID))
                .willReturn(userDto);
        given(postService.getPostDtoById(postId))
                .willThrow(new BusinessException(code));

        //when
        mvc.perform(delete("/api/v1/posts/" + postId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        //then
        then(userService).should()
                .getUserDtoById(any());
        then(postService).should()
                .getPostDtoById(any());
        then(postService).should(never())
                .isValidate(any(), any());
        then(postService).should(never())
                .deletePost(any());
    }

    @DisplayName("[DELETE] 삭제 권한이 없는 유저가 게시글 삭제 요청시 예외 처리 - 실패")
    @Test
    void given_NotAuthorizationUserRequest_When_DeletePost_Then_ForbiddenException() throws Exception {
        //given
        Long postId = 1L;
        PostDto postDto = createPostDto(1L, 99L);
        UserDto userDto = createUserDto();
        PostErrorCode code = PostErrorCode.FORBIDDEN;

        given(userService.getUserDtoById(NumberConstants.USER_ID))
                .willReturn(userDto);
        given(postService.getPostDtoById(postId))
                .willReturn(postDto);
        willThrow(new BusinessException(code)).given(postService)
                .isValidate(postDto, userDto);

        //when
        mvc.perform(delete("/api/v1/posts/" + postId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        //then
        then(userService).should()
                .getUserDtoById(any());
        then(postService).should()
                .getPostDtoById(any());
        then(postService).should()
                .isValidate(any(), any());
        then(postService).should(never())
                .deletePost(any());
    }

    @DisplayName("[GET] 카테고리: 자유게시판, '카테고리와 cursor값에 따른 다음 게시글 목록' 조회")
    @Test
    void given_CategoryAndCursor_When_GetPostsByCategory_Then_ResponsePagingPosts() throws Exception {
        List<PostDto> postDtos = new ArrayList<>();
        int category = 1;
        Long cursor = 1L;
        for (int i = 1; i <= NumberConstants.POST_PAGINATION_SIZE; i++) postDtos.add(createPostDto((long) i));
        PostsWithCursorResponse response = PostsWithCursorResponse.of(postDtos.stream()
                .map(PostsResponse::of)
                .toList(), cursor);

        PostMessage code = PostMessage.POSTS_READ;

        given(postService.getPostDtosByCategory(
                1,
                cursor,
                PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE))
        ).willReturn(postDtos);

        //when
        mvc.perform(get("/api/v1/posts/category/" + category)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.posts[0].id").value(response.posts()
                        .get(0)
                        .id()))
                .andExpect(jsonPath("$.data.posts[1].id").value(response.posts()
                        .get(1)
                        .id()))
                .andExpect(jsonPath("$.data.posts[9].id").value(response.posts()
                        .get(9)
                        .id()))
                .andExpect(jsonPath("$.data.cursor").value(response.cursor()))
        ;

        then(postService).should()
                .getPostDtosByCategory(
                        1,
                        cursor,
                        PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE)
                );
    }

    @DisplayName("[GET] 카테고리: 자유게시판, '카테고리와 cursor값에 따른 다음 게시글 목록' 조회 cursor값(첫 페이지가 아니고, 조회된 게시글 없다면 -1L)'을 돌려준다.")
    @Test
    void given_CategoryAndCursor_When_GetPostsByCategory_Then_ResponseNotFoundPostValue() throws Exception {
        List<PostDto> postDtos = new ArrayList<>();
        int category = 1;
        Long cursor = 1L;
        PostsWithCursorResponse response = PostsWithCursorResponse.of(postDtos.stream()
                .map(PostsResponse::of)
                .toList(), cursor);

        PostMessage code = PostMessage.POSTS_READ;

        given(postService.getPostDtosByCategory(
                1,
                cursor,
                PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE))
        ).willReturn(postDtos);

        //when
        mvc.perform(get("/api/v1/posts/category/" + category)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.cursor").value(NumberConstants.LAST_CURSOR))
        ;

        then(postService).should()
                .getPostDtosByCategory(
                        1,
                        cursor,
                        PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE)
                );
    }

    @DisplayName("[GET] 검색어와 cursor값을 가지고 게시글 목록 및 cursor값 조회")
    @Test
    void given_SearchWordAndCursor_When_GetPostsBySearchWord_Then_ResponsePagingPosts() throws Exception {
        Long cursor = 1L;
        String searchWord = "title";
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        List<PostDto> postDtos = new ArrayList<>();
        for (int i = 1; i <= NumberConstants.POST_PAGINATION_SIZE; i++) postDtos.add(createPostDto((long) i));
        PostsWithCursorResponse response = PostsWithCursorResponse.of(postDtos.stream()
                .map(PostsResponse::of)
                .toList(), cursor);

        PostMessage code = PostMessage.POSTS_READ;

        given(postService.getPostDtosBySearchWord(
                searchWord,
                cursor,
                pageable)
        ).willReturn(postDtos);

        //when
        mvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("searchWord", searchWord)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.posts[0].id").value(response.posts()
                        .get(0)
                        .id()))
                .andExpect(jsonPath("$.data.posts[1].id").value(response.posts()
                        .get(1)
                        .id()))
                .andExpect(jsonPath("$.data.posts[9].id").value(response.posts()
                        .get(9)
                        .id()))
                .andExpect(jsonPath("$.data.cursor").value(response.cursor()))
        ;

        then(postService).should()
                .getPostDtosBySearchWord(
                        searchWord,
                        cursor,
                        PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE)
                );
    }

    @DisplayName("[GET] 검색어와 cursor값을 가지고 게시글 목록 및 cursor값 조회 cursor값(첫 페이지가 아니고, 조회된 게시글 없다면 -1L)'을 돌려준다.")
    @Test
    void given_SearchWordAndCursor_When_GetPostsBySearchWord_Then_ResponseNotFoundPostValue() throws Exception {
        Long cursor = 1L;
        String searchWord = "title";
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        List<PostDto> postDtos = new ArrayList<>();

        PostMessage code = PostMessage.POSTS_READ;

        given(postService.getPostDtosBySearchWord(
                searchWord,
                cursor,
                pageable)
        ).willReturn(postDtos);

        //when
        mvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("searchWord", searchWord)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.cursor").value(-1))
        ;

        then(postService).should()
                .getPostDtosBySearchWord(
                        searchWord,
                        cursor,
                        PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE)
                );
    }

    @DisplayName("[GET] 검색어와 cursor값을 가지고 게시글 목록 및 cursor값 조회 cursor값(첫 페이지면서, 조회된 게시글 없다면 -2L)'을 돌려준다.")
    @Test
    void given_SearchWordAndCursor_When_FirstPageOfGetPostsBySearchWord_Then_ResponseNotFoundPostValue() throws Exception {
        Long cursor = NumberConstants.FIRST_CURSOR; // 0L
        String searchWord = "title";
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        List<PostDto> postDtos = new ArrayList<>();

        PostMessage code = PostMessage.POSTS_READ;

        given(postService.getPostDtosBySearchWord(
                searchWord,
                cursor,
                pageable)
        ).willReturn(postDtos);

        //when
        mvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("searchWord", searchWord)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.cursor").value(NumberConstants.NOTHING_CURSOR)) // -2L
        ;

        then(postService).should()
                .getPostDtosBySearchWord(
                        searchWord,
                        cursor,
                        PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE)
                );
    }

    @DisplayName("[GET] 인기 게시글 목록 조회 - page값을 받아, 'page번호에 따른 인기 게시글 목록'과 page값 반환")
    @Test
    void given_PageNumber_When_GetHotPosts_Then_ResponseHotPostsAndPageValue() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        PostMessage code = PostMessage.POSTS_READ;
        List<PostDto> postDtos = new ArrayList<>();
        for (int i = 1; i <= NumberConstants.POST_PAGINATION_SIZE; i++) postDtos.add(createPostDto((long) i));

        var postsResponse = postDtos.stream()
                .map(PostsResponse::of)
                .toList();
        Page<PostDto> pages = new PageImpl<>(postDtos, pageable, 10);

        given(postService.getHotPostDtos(pageable)).willReturn(pages);
        given(postService.getNextPage(pages, 0)).willReturn(1);

        PostsWithPageResponse response = PostsWithPageResponse.of(postsResponse, 1);

        mvc.perform(get("/api/v1/posts/hot")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.posts[0].id").value(response.posts()
                        .get(0)
                        .id()))
                .andExpect(jsonPath("$.data.posts[9].id").value(response.posts()
                        .get(9)
                        .id()))
                .andExpect(jsonPath("$.data.next_page").value(response.nextPage()))
        ;
    }

    @DisplayName("[GET] 인기 게시글 목록 조회 - page값을 받아, 'page번호에 따른 인기 게시글 목록'과 마지막 page number인 -1 반환")
    @Test
    void given_PageNumber_When_GetHotPosts_Then_ResponseHotPostsAndLastPageValue() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        PostMessage code = PostMessage.POSTS_READ;
        List<PostDto> postDtos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) postDtos.add(createPostDto((long) i));

        var postsResponse = postDtos.stream()
                .map(PostsResponse::of)
                .toList();
        Page<PostDto> pages = new PageImpl<>(postDtos, pageable, 10);
        given(postService.getHotPostDtos(pageable)).willReturn(pages);
        given(postService.getNextPage(pages, 0)).willReturn(-1);

        PostsWithPageResponse response = PostsWithPageResponse.of(postsResponse, -1);

        mvc.perform(get("/api/v1/posts/hot")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.next_page").value(response.nextPage()))
        ;
    }

    private PostUpdateRequest createPostUpdateRequest() {
        return PostUpdateRequest.of(
                1L,
                "title",
                "content",
                List.of(),
                1
        );
    }

    private PostCreateRequest createPostCreateRequest() {
        return PostCreateRequest.of(
                "title",
                "content",
                "",
                1
        );
    }

    private PostDto createPostDto(Long id) {
        return PostDto.of(
                id,
                "title",
                "content",
                0,
                0,
                0,
                "",
                1,
                createUserDto(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private PostDto createPostDto(Long postId, Long userId) {
        return PostDto.of(
                postId,
                "title",
                "content",
                0,
                0,
                0,
                "",
                1,
                createUserDto(userId),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private UserDto createUserDto() {
        return UserDto.of(
                NumberConstants.USER_ID,
                "mail@mail",
                "user",
                "{noop}password",
                1,
                1,
                null,
                "bio",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private UserDto createUserDto(Long userId) {
        return UserDto.of(
                userId,
                "mail@mail",
                "user",
                "{noop}password",
                1,
                1,
                null,
                "bio",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}