package org.fastcampus.oruryclient.post.service;

import org.fastcampus.orurycommon.error.code.PostErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurycommon.util.S3Repository;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.post.db.model.Post;
import org.fastcampus.orurydomain.post.db.repository.PostRepository;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] 게시글 테스트")
@ActiveProfiles("test")
class PostServiceTest {

    private PostRepository postRepository;
    private S3Repository s3Repository;
    private PostService postService;

    @BeforeEach
    public void setUp() {
        postRepository = mock(PostRepository.class);
        s3Repository = mock(S3Repository.class);
        postService = new PostService(postRepository, s3Repository);
    }

    @Test
    @DisplayName("게시글이 성공적으로 생성되어야 한다.")
    void should_PostCreateSuccessfully() {
        // given
        UserDto userDto = createUserDto(1L);
        PostDto postDto = createPostDto(1L, 1L);
        // when
        postService.createPost(postDto);

        // then
        verify(postRepository).save(postDto.toEntity());
    }

    @Test
    @DisplayName("게시글을 수정하면, 정상적으로 반영된다.")
    void when_UpdatePost_Then_SavePost() {
        // given
        PostDto existingPostDto = createPostDto(1L, 1L);

        // when
        postService.updatePost(existingPostDto);

        // then
        then(postRepository).should(times(1))
                .save(existingPostDto.toEntity());
    }

    @Test
    @DisplayName("다음 페이지가 존재하면 다음 페이지를 return해준다.")
    void when_ExistNextPage_Then_NextPageNumber() {
        // given
        int page = 1;
        Page<PostDto> postDtos = mock(Page.class);
        given(postDtos.hasNext()).willReturn(true); // 다음 페이지가 있다고 가정

        // when
        int nextPage = postService.getNextPage(postDtos, page);

        // then
        assertEquals(page + 1, nextPage);
    }

    @Test
    @DisplayName("다음 페이지가 존재하지 않으면 -1를 return해준다.")
    void when_NotExistNextPage_Then_LastPage() {
        // given
        int page = 1;
        Page<PostDto> postDtos = mock(Page.class);
        given(postDtos.hasNext()).willReturn(false); // 다음 페이지가 없다고 가정

        // when
        int nextPage = postService.getNextPage(postDtos, page);

        // then
        assertEquals(-1, nextPage);
    }

    @Test
    @DisplayName("최근 한달, 좋아요가 10개 이상인 인기 게시글을 조회한다.")
    void when_RetrieveHotPosts_Then_OverThan10LikesAndRecentMonthPosts() {
        // given
        Pageable pageable = mock(Pageable.class);
        Page<Post> mockPostPage = mock(Page.class);

        // 시간 값에 대해 엄격히 테스트하지 않겠다는 것을 명시하는 문법. lenient()
        lenient().when(postRepository.findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(anyInt(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(mockPostPage);

        // when
        postService.getHotPostDtos(pageable);

        // then
        verify(postRepository, times(1)).findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(anyInt(), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    @DisplayName("게시글을 수정/삭제할 때, 작성자와 로그인 유저가 동일인물이라면 403 Exception을 throw하지 않는다.")
    void when_ModifyOrDeletePostWithInvalidUser_Then_DontThrowException() {
        // given
        UserDto userDto = createUserDto(1L);
        PostDto postDto = createPostDto(1L, 1L);

        // when & then
        assertDoesNotThrow(
                () -> postService.isValidate(postDto, userDto)
        );
    }

    @Test
    @DisplayName("게시글을 수정/삭제할 때, 작성자와 로그인 유저가 동일인물이 아니라면 403 Exception을 throw한다.")
    void when_ModifyOrDeletePostWithInvalidUser_Then_ModifySuccessfully() {
        // given
        UserDto userDto = createUserDto(1L);
        PostDto postDto = createPostDto(1L, 2L);

        // when & then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> postService.isValidate(postDto, userDto)
        );

        assertEquals(PostErrorCode.FORBIDDEN.getStatus(), exception.getStatus());
    }

    @Test
    @DisplayName("존재하는 게시글을 조회 요청하면 postDto를 가져온다.")
    void when_RetrieveExistingPost_Then_GetPostDto() {
        // given
        UserDto userDto = createUserDto(1L);
        PostDto existingPostDto = createPostDto(1L, 1L);
        List<String> postImages = List.of("post1.png", "post2.png");
        List<String> postUserImage = List.of("user.png");

        when(postRepository.findById(any())).thenReturn(Optional.of(existingPostDto.toEntity()));
        when(s3Repository.getUrls(eq("post"), anyString())).thenReturn(postImages);
        when(s3Repository.getUrls(eq("user"), anyString())).thenReturn(postUserImage);

        // when
        PostDto resultPostDto = postService.getPostDtoById(1L);

        // then
        then(postRepository).should(times(1))
                .findById(existingPostDto.id());
    }

    @Test
    @DisplayName("존재하는 게시글을 조회 시 게시글 조회수가 늘어나야 한다.")
    void when_RetrieveExistingPost_Then_updateViewCount() {
        // given
        UserDto userDto = createUserDto(1L);
        PostDto existingPostDto = createPostDto(1L, 1L);
        List<String> postImages = List.of("post1.png", "post2.png");
        List<String> postUserImage = List.of("user.png");

        when(postRepository.findById(any())).thenReturn(Optional.of(existingPostDto.toEntity()));
        when(s3Repository.getUrls(eq("post"), anyString())).thenReturn(postImages);
        when(s3Repository.getUrls(eq("user"), anyString())).thenReturn(postUserImage);

        // when
        PostDto resultPostDto = postService.getPostDtoById(1L);
        postService.addViewCount(existingPostDto);

        // then
        then(postRepository).should(times(1))
                .updateViewCount(existingPostDto.id());
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회하면 PostErrorCode.NOT_FOUND 예외를 던진다.")
    void when_RetrieveNonExistingPost_Then_ThrowNotFoundException() {
        // given
        when(postRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> postService.getPostDtoById(1000L)
        );

        assertEquals(PostErrorCode.NOT_FOUND.getStatus(), exception.getStatus());
    }

    @Test
    @DisplayName("카테고리 검색 시 첫 페이지인 경우, 최대 10개까지 게시글을 가져온다.")
    void when_RetrieveCategoryWithFirstCursor_Then_GetMaximum10Posts() {
        // given
        int category = 1;
        Long cursor = 0L;
        Pageable pageable = PageRequest.of(0, 10);

        List<PostDto> postDtos = Arrays.asList(
                createPostDto(1L, 1L),
                createPostDto(2L, 2L),
                createPostDto(3L, 1L),
                createPostDto(4L, 1L),
                createPostDto(5L, 3L),
                createPostDto(6L, 3L),
                createPostDto(7L, 4L),
                createPostDto(8L, 1L),
                createPostDto(9L, 5L),
                createPostDto(10L, 2L)
        );
        List<Post> posts = Arrays.asList(
                createPost(1L, 1L),
                createPost(2L, 2L),
                createPost(3L, 1L),
                createPost(4L, 1L),
                createPost(5L, 3L),
                createPost(6L, 3L),
                createPost(7L, 4L),
                createPost(8L, 1L),
                createPost(9L, 5L),
                createPost(10L, 2L)
        );
        List<String> postImages = List.of("post1.png", "post2.png");
        List<String> postUserImage = List.of("user.png");

        when(postRepository.findByCategoryOrderByIdDesc(category, pageable)).thenReturn(posts);
        when(s3Repository.getUrls(eq("post"), anyString())).thenReturn(postImages);
        when(s3Repository.getUrls(eq("user"), anyString())).thenReturn(postUserImage);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosByCategory(category, cursor, PageRequest.of(0, 10));

        // then
        verify(postRepository).findByCategoryOrderByIdDesc(category, pageable);
        assertEquals(postDtos.size(), resultPostDtos.size());
    }

    @Test
    @DisplayName("커서와 카테고리를 입력받아 카테고리 내 게시글을 paging 처리하여 보여준다.")
    void when_InputCursorAndCategory_Then_ShowPostOfCategoryAsPaging() {
        // given
        int category = 1;
        Long cursor = 2L;
        Pageable pageable = PageRequest.of(0, 10);

        List<PostDto> postDtos = Arrays.asList(
                createPostDto(1L, 1L),
                createPostDto(2L, 1L)
        );
        List<Post> posts = Arrays.asList(
                createPost(1L, 1L),
                createPost(2L, 1L)
        );
        List<String> postImages = List.of("post1.png", "post2.png");
        List<String> postUserImage = List.of("user.png");

        when(postRepository.findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable)).thenReturn(posts);
        when(s3Repository.getUrls(eq("post"), anyString())).thenReturn(postImages);
        when(s3Repository.getUrls(eq("user"), anyString())).thenReturn(postUserImage);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosByCategory(category, cursor, PageRequest.of(0, 10));

        // then
        verify(postRepository).findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable);
        assertEquals(postDtos.size(), resultPostDtos.size());
    }

    @Test
    @DisplayName("검색어 검색 시 첫 페이지인 경우, 최대 10개까지 게시글을 가져온다.")
    void when_RetrieveSearchWordWithFirstCursor_Then_GetMaximum10Posts() {
        // given
        Long cursor = 0L;
        String searchWord = "title";
        Pageable pageable = PageRequest.of(0, 10);

        List<PostDto> postDtos = Arrays.asList(
                createPostDto(1L, 1L),
                createPostDto(2L, 2L),
                createPostDto(3L, 1L),
                createPostDto(4L, 1L),
                createPostDto(5L, 3L),
                createPostDto(6L, 3L),
                createPostDto(7L, 4L),
                createPostDto(8L, 1L),
                createPostDto(9L, 5L),
                createPostDto(10L, 2L)
        );
        List<Post> posts = Arrays.asList(
                createPost(1L, 1L),
                createPost(2L, 2L),
                createPost(3L, 1L),
                createPost(4L, 1L),
                createPost(5L, 3L),
                createPost(6L, 3L),
                createPost(7L, 4L),
                createPost(8L, 1L),
                createPost(9L, 5L),
                createPost(10L, 2L)
        );
        List<String> postImages = List.of("post1.png", "post2.png");
        List<String> postUserImage = List.of("user.png");

        when(postRepository.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, searchWord, pageable)).thenReturn(posts);
        when(s3Repository.getUrls(eq("post"), anyString())).thenReturn(postImages);
        when(s3Repository.getUrls(eq("user"), anyString())).thenReturn(postUserImage);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosBySearchWord(searchWord, cursor, PageRequest.of(0, 10));

        // then
        verify(postRepository).findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, searchWord, pageable);
        assertEquals(postDtos.size(), resultPostDtos.size());
    }

    @Test
    @DisplayName("커서와 검색어를 입력받아 제목이나 본문이 검색어를 포함하는 게시글을 보여준다.")
    void when_InputCursorAndSearchWord_Then_ShowPostThatContainTitleOrContent() {
        // given
        Long cursor = 2L;
        String searchWord = "title";
        Pageable pageable = PageRequest.of(0, 10);

        UserDto userDto = createUserDto(1L);

        List<PostDto> postDtos = Arrays.asList(
                createPostDto(1L, 1L),
                createPostDto(2L, 1L)
        );
        List<Post> posts = Arrays.asList(
                createPost(1L, 1L),
                createPost(2L, 1L)
        );
        List<String> postImages = List.of("post1.png", "post2.png");
        List<String> postUserImage = List.of("user.png");

        when(postRepository.findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(cursor, searchWord, cursor, searchWord, pageable)).thenReturn(posts);
        when(s3Repository.getUrls(eq("post"), anyString())).thenReturn(postImages);
        when(s3Repository.getUrls(eq("user"), anyString())).thenReturn(postUserImage);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosBySearchWord(searchWord, cursor, PageRequest.of(0, 10));

        // then
        verify(postRepository).findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(cursor, searchWord, cursor, searchWord, pageable);
        assertEquals(postDtos.size(), resultPostDtos.size());
    }

    // getPostDtosByUserId 테스트 추가
    @Test
    @DisplayName("userId, cursor, pageable을 받아 첫번째 커서일 때 findByUserIdOrderByIdDesc을 실행한다.")
    void given_firstCursor_when_getMyPosts_then_successfully() {
        // given
        Long cursor = NumberConstants.FIRST_CURSOR;
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);

        List<Post> posts = Arrays.asList(
                createPost(10L, NumberConstants.USER_ID),
                createPost(9L, NumberConstants.USER_ID),
                createPost(8L, NumberConstants.USER_ID),
                createPost(7L, NumberConstants.USER_ID),
                createPost(6L, NumberConstants.USER_ID),
                createPost(5L, NumberConstants.USER_ID),
                createPost(4L, NumberConstants.USER_ID),
                createPost(3L, NumberConstants.USER_ID),
                createPost(2L, NumberConstants.USER_ID),
                createPost(1L, NumberConstants.USER_ID)
        );
        List<String> postImages = List.of("post1.png", "post2.png");
        List<String> postUserImage = List.of("user.png");
        List<PostDto> expectPostDtos = posts.stream()
                .map(post -> PostDto.from(post, ImageUrlConverter.convertListToString(postImages), postUserImage.get(0)))
                .toList();

        given(postRepository.findByUserIdOrderByIdDesc(anyLong(), any())).willReturn(posts);
        given(s3Repository.getUrls(eq("post"), anyString())).willReturn(postImages);
        given(s3Repository.getUrls(eq("user"), anyString())).willReturn(postUserImage);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosByUserId(NumberConstants.USER_ID, cursor, pageable);

        // then
        assertEquals(resultPostDtos, expectPostDtos);
        then(postRepository).should(times(1)).findByUserIdOrderByIdDesc(anyLong(), any());
        then(postRepository).should(times(0)).findByUserIdAndIdLessThanOrderByIdDesc(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("userId, cursor, pageable을 받아 첫번째 커서가 아닐 때 findByUserIdAndIdLessThanOrderByIdDesc을 실행한다.")
    void given_NotFirstCursor_when_getMyPosts_then_successfully() {
        // given
        Long cursor = 20L;
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);

        List<Post> posts = Arrays.asList(
                createPost(19L, NumberConstants.USER_ID),
                createPost(18L, NumberConstants.USER_ID),
                createPost(17L, NumberConstants.USER_ID),
                createPost(16L, NumberConstants.USER_ID),
                createPost(15L, NumberConstants.USER_ID),
                createPost(14L, NumberConstants.USER_ID),
                createPost(13L, NumberConstants.USER_ID),
                createPost(12L, NumberConstants.USER_ID),
                createPost(11L, NumberConstants.USER_ID),
                createPost(10L, NumberConstants.USER_ID)
        );
        List<String> postImages = List.of("post1.png", "post2.png");
        List<String> postUserImage = List.of("user.png");
        List<PostDto> expectPostDtos = posts.stream()
                .map(post -> PostDto.from(post, ImageUrlConverter.convertListToString(postImages), postUserImage.get(0)))
                .toList();


        given(postRepository.findByUserIdAndIdLessThanOrderByIdDesc(anyLong(), anyLong(), any())).willReturn(posts);
        given(s3Repository.getUrls(eq("post"), anyString())).willReturn(postImages);
        given(s3Repository.getUrls(eq("user"), anyString())).willReturn(postUserImage);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosByUserId(NumberConstants.USER_ID, cursor, pageable);

        // then
        assertEquals(expectPostDtos, resultPostDtos);
        then(postRepository).should(times(0)).findByUserIdOrderByIdDesc(anyLong(), any());
        then(postRepository).should(times(1)).findByUserIdAndIdLessThanOrderByIdDesc(anyLong(), anyLong(), any());
    }

    private static Post createPost(Long postId, Long userId) {
        return Post.of(
                postId,
                "title",
                "content",
                0,
                0,
                0,
                "image.jpg",
                1,
                createPostDto(postId, userId).userDto()
                        .toEntity(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static PostDto createPostDto(Long postId, Long userId) {
        return PostDto.of(
                1L,
                "title",
                "content",
                0,
                0,
                0,
                "image.jpg",
                1,
                createUserDto(userId),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static UserDto createUserDto(Long id) {
        return UserDto.of(
                id,
                "test@test.com",
                "test",
                "password",
                1,
                1,
                LocalDate.now(),
                "test.jpg",
                LocalDateTime.now(),
                LocalDateTime.now(),
                NumberConstants.IS_NOT_DELETED
        );
    }

    private static User createUser() {
        return User.of(
                1L,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "userProfileImage",
                LocalDateTime.of(1999, 3, 1, 7, 50),
                LocalDateTime.of(1999, 3, 1, 7, 50),
                NumberConstants.IS_NOT_DELETED
        );
    }
}
