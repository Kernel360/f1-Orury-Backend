package org.orury.client.post.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.ServiceTest;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.DomainFixtureFactory;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.domain.dto.PostLikeDto;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.post.domain.entity.PostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.orury.common.error.code.PostErrorCode.FORBIDDEN;
import static org.orury.common.error.code.PostErrorCode.NOT_FOUND;
import static org.orury.common.util.S3Folder.POST;
import static org.orury.domain.DomainFixtureFactory.TestPostLikeDto.createPostLikeDto;
import static org.orury.domain.DomainFixtureFactory.TestUserDto.createUserDto;
import static org.orury.domain.global.constants.NumberConstants.POST_PAGINATION_SIZE;
import static org.orury.domain.global.constants.NumberConstants.USER_ID;

@DisplayName("[Service] 게시글 테스트")
class PostServiceImplTest extends ServiceTest {

    @Test
    @DisplayName("게시글이 성공적으로 생성되어야 한다.")
    void should_PostCreateSuccessfully() {
        // given
        PostDto postDto = createPostDto(null, 1L);
        List<MultipartFile> files = List.of(createImagePart());
        var images = List.of("post1.png", "post2.png");
        given(imageAsyncStore.upload(POST, files)).willReturn(images);

        // when
        postService.createPost(postDto, files);

        // then
        then(imageAsyncStore).should(times(1)).upload(POST, files);
    }

    @Test
    @DisplayName("게시글을 수정하면, 정상적으로 반영된다.")
    void when_UpdatePost_Then_SavePost() {
        // given
        PostDto postDto = createPostDto(1L, 1L);
        List<MultipartFile> files = List.of(createImagePart());
        var images = List.of("post1.png", "post2.png");

        // when
        postService.createPost(postDto, files);

        //then
        then(imageStore).should(times(1)).delete(POST, postDto.images());
        then(imageAsyncStore).should(times(1)).upload(POST, files);
        then(postStore).should(times(1)).save(postDto.toEntity(images));
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

    //
    @Test
    @DisplayName("최근 한달, 좋아요가 10개 이상인 인기 게시글을 조회한다.")
    void when_RetrieveHotPosts_Then_OverThan10LikesAndRecentMonthPosts() {
        // given
        Pageable pageable = mock(Pageable.class);
        Page<Post> mockPostPage = mock(Page.class);

        // 시간 값에 대해 엄격히 테스트하지 않겠다는 것을 명시하는 문법. lenient()
        lenient()
                .when(postReader.findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(
                        anyInt(),
                        any(LocalDateTime.class),
                        any(Pageable.class)))
                .thenReturn(mockPostPage);

        // when
        postService.getHotPostDtos(pageable);

        // then
        then(postReader).should(times(1))
                .findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(anyInt(), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    @DisplayName("게시글을 수정/삭제할 때, 작성자와 로그인 유저가 동일인물이라면 403 Exception을 throw하지 않는다.")
    void when_ModifyOrDeletePostWithInvalidUser_Then_DontThrowException() {
        // given
        var post = createPost(1L, 1L);
        given(postReader.findById(1L)).willReturn(Optional.of(post));

        //when
        postService.getPostDtoById(1L, 1L);

        //then
        assertDoesNotThrow(() -> new BusinessException(FORBIDDEN));
        then(postReader).should(times(1)).findById(1L);
    }

    @Test
    @DisplayName("게시글을 수정/삭제할 때, 작성자와 로그인 유저가 동일인물이 아니라면 403 Exception을 throw한다.")
    void when_ModifyOrDeletePostWithInvalidUser_Then_ModifySuccessfully() {
        // given
        var post = createPost(1L, 1L);
        given(postReader.findById(1L)).willReturn(Optional.of(post));

        //when
        try {
            postService.getPostDtoById(2L, 1L);
            fail();
        } catch (BusinessException e) {
            assertThat(e.getMessage()).isEqualTo(FORBIDDEN.getMessage());
        }

        //then
        then(postReader).should(times(1)).findById(1L);
    }

    @Test
    @DisplayName("존재하는 게시글을 조회 요청하면 postDto를 가져온다.")
    void when_RetrieveExistingPost_Then_GetPostDto() {
        // given
        PostDto expect = createPostDto(1L, 1L);
        Post post = expect.toEntity();
        String userImage = "test.png";

        given(postReader.findById(1L)).willReturn(Optional.of(post));
        given(postReader.isPostLiked(any(), any())).willReturn(false);

        // when
        var actual = postService.getPostDtoById(1L);
        assertThat(actual)
                .isNotNull()
                .isEqualTo(expect);

        // then
        then(postReader).should(times(1)).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회하면 PostErrorCode.NOT_FOUND 예외를 던진다.")
    void when_RetrieveNonExistingPost_Then_ThrowNotFoundException() {
        // given
        given(postReader.findById(any())).willThrow(new BusinessException(NOT_FOUND));

        // when & then
        try {
            postService.getPostDtoById(anyLong());
        } catch (BusinessException e) {
            assertThat(e.getMessage()).isEqualTo(NOT_FOUND.getMessage());
        }
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
        given(postReader.findByCategoryOrderByIdDesc(category, 0L, pageable)).willReturn(posts);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosByCategory(category, cursor, PageRequest.of(0, 10));

        // then
        verify(postReader).findByCategoryOrderByIdDesc(category, 0L, pageable);
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

        when(postReader.findByCategoryOrderByIdDesc(category, cursor, pageable)).thenReturn(posts);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosByCategory(category, cursor, PageRequest.of(0, 10));

        // then
        verify(postReader).findByCategoryOrderByIdDesc(category, cursor, pageable);
        assertEquals(postDtos.size(), resultPostDtos.size());
    }

    //
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

        when(postReader.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, cursor, pageable)).thenReturn(posts);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosBySearchWord(searchWord, cursor, PageRequest.of(0, 10));

        // then
        verify(postReader).findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, cursor, pageable);
        assertEquals(postDtos.size(), resultPostDtos.size());
    }

    @Test
    @DisplayName("커서와 검색어를 입력받아 제목이나 본문이 검색어를 포함하는 게시글을 보여준다.")
    void when_InputCursorAndSearchWord_Then_ShowPostThatContainTitleOrContent() {
        // given
        Long cursor = 2L;
        String searchWord = "title";
        Pageable pageable = PageRequest.of(0, 10);

        List<PostDto> postDtos = Arrays.asList(
                createPostDto(1L, 1L),
                createPostDto(2L, 1L)
        );
        List<Post> posts = Arrays.asList(
                createPost(1L, 1L),
                createPost(2L, 1L)
        );

        when(postReader.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, cursor, pageable)).thenReturn(posts);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosBySearchWord(searchWord, cursor, PageRequest.of(0, 10));

        // then
        verify(postReader).findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, cursor, pageable);
        assertEquals(postDtos.size(), resultPostDtos.size());
    }

    // getPostDtosByUserId 테스트 추가
    @Test
    @DisplayName("userId, cursor, pageable을 받아 첫번째 커서일 때 findByUserIdOrderByIdDesc을 실행한다.")
    void given_firstCursor_when_getMyPosts_then_successfully() {
        // given
        Long cursor = NumberConstants.FIRST_CURSOR;
        Pageable pageable = PageRequest.of(0, POST_PAGINATION_SIZE);

        List<Post> posts = Arrays.asList(
                createPost(10L, USER_ID),
                createPost(9L, USER_ID),
                createPost(8L, USER_ID),
                createPost(7L, USER_ID),
                createPost(6L, USER_ID),
                createPost(5L, USER_ID),
                createPost(4L, USER_ID),
                createPost(3L, USER_ID),
                createPost(2L, USER_ID),
                createPost(1L, USER_ID)
        );
        List<PostDto> expectPostDtos = posts.stream()
                .map(post -> PostDto.from(post, false))
                .toList();

        given(postReader.findByUserIdOrderByIdDesc(anyLong(), anyLong(), any())).willReturn(posts);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosByUserId(USER_ID, cursor, pageable);

        // then
        assertEquals(resultPostDtos, expectPostDtos);
        then(postReader).should(times(1)).findByUserIdOrderByIdDesc(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("userId, cursor, pageable을 받아 첫번째 커서가 아닐 때 findByUserIdAndIdLessThanOrderByIdDesc을 실행한다.")
    void given_NotFirstCursor_when_getMyPosts_then_successfully() {
        // given
        Long cursor = 20L;
        Pageable pageable = PageRequest.of(0, POST_PAGINATION_SIZE);

        List<Post> posts = Arrays.asList(
                createPost(19L, USER_ID),
                createPost(18L, USER_ID),
                createPost(17L, USER_ID),
                createPost(16L, USER_ID),
                createPost(15L, USER_ID),
                createPost(14L, USER_ID),
                createPost(13L, USER_ID),
                createPost(12L, USER_ID),
                createPost(11L, USER_ID),
                createPost(10L, USER_ID)
        );

        List<PostDto> expectPostDtos = posts.stream()
                .map(post -> PostDto.from(post, false))
                .toList();

        given(postReader.findByUserIdOrderByIdDesc(anyLong(), anyLong(), any())).willReturn(posts);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosByUserId(USER_ID, cursor, pageable);

        // then
        assertEquals(expectPostDtos, resultPostDtos);
        then(postReader).should(times(1)).findByUserIdOrderByIdDesc(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("유저가 게시글에 좋아요를 누르면 좋아요 테이블에 데이터가 생성")
    void when_UserPostLike_Then_CreatePostLikeSuccessFully() {
        // given
        PostLikeDto postLikeDto = createPostLikeDto().build().get();
        PostLike postLike = postLikeDto.toEntity();
        given(postReader.existsByPostLikePK(any())).willReturn(false);

        // when
        postService.createPostLike(postLikeDto);

        // then
        verify(postStore, times(1)).save(postLike);
    }

    @Test
    @DisplayName("유저가 게시글에 좋아요를 누르면 좋아요 테이블에 데이터가 삭제")
    void when_UserPostLike_Then_DeletePostLikeSuccessFully() {
        // given
        PostLikeDto postLikeDto = createPostLikeDto().build().get();
        PostLike postLike = postLikeDto.toEntity();
        given(postReader.existsByPostLikePK(any())).willReturn(true);

        // when
        postService.deletePostLike(postLikeDto);

        // then
        verify(postStore, times(1)).delete(postLike);
    }

    private MockMultipartFile createImagePart() {
        return new MockMultipartFile(
                "TestImageName",
                "testImageFileName",
                MediaType.TEXT_PLAIN_VALUE,
                "testImageFileDate".getBytes()
        );
    }

    private static Post createPost(Long postId, Long userId) {
        return Post.of(
                postId,
                "title",
                "content",
                0,
                0,
                0,
                List.of("post1.png", "post2.png"),
                1,
                createPostDto(postId, userId).userDto().toEntity(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static PostDto createPostDto(Long postId, Long userId) {
        return DomainFixtureFactory.TestPostDto.createPostDto()
                .id(postId)
                .userDto(createUserDto()
                        .id(userId).build().get())
                .build().get();
    }
}
