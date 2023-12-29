package org.fastcampus.oruryclient.post.service;

import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.oruryclient.post.error.PostErrorCode;
import org.fastcampus.orurydomain.post.db.model.Post;
import org.fastcampus.orurydomain.post.db.repository.PostRepository;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostServiceTest")
@ActiveProfiles("test")
class PostServiceTest {

    private PostRepository postRepository;
    private PostService postService;

    @BeforeEach
    public void setUp() {
        postRepository = mock(PostRepository.class);
        postService = new PostService(postRepository);
    }

    @Test
    @DisplayName("게시글이 성공적으로 생성되어야 한다.")
    void should_PostCreateSuccessfully() {
        // given
        UserDto userDto = UserDto.of(
                1L,
                "test@test.com",
                "test",
                "password",
                1,
                1,
                LocalDate.now(),
                "test.jpg",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PostDto postDto = PostDto.of(
                1L,
                "title",
                "content",
                0,
                0,
                0,
                "image.jpg",
                1,
                userDto,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        postService.createPost(postDto);

        // then
        verify(postRepository).save(postDto.toEntity());
    }

    @Test
    @DisplayName("존재하는 게시글을 수정하면, 게시글이 정상적으로 수정되어야 한다.")
    void when_ModifyExistingPost_Then_ModifySuccessfully() {
        // given
        UserDto userDto = UserDto.of(
                1L,
                "test@test.com",
                "test",
                "password",
                1,
                1,
                LocalDate.now(),
                "test.jpg",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PostDto existingPostDto = PostDto.of(
                1L,
                "title",
                "content",
                0,
                0,
                0,
                "image.jpg",
                1,
                userDto,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PostDto modifiedPostDto = PostDto.of(
                1L,
                "modified title",
                "modified content",
                0,
                0,
                0,
                "image.jpg",
                1,
                userDto,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(postRepository.findById(any())).thenReturn(Optional.of(existingPostDto.toEntity()));

        // when
        postService.getPostDtoById(1L);
        postService.updatePost(modifiedPostDto);

        // then
        verify(postRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("존재하는 게시글을 조회 요청하면 postDto를 가져온다.")
    void when_RetrieveExistingPost_Then_GetPostDto() {
        // given
        UserDto userDto = UserDto.of(
                1L,
                "test@test.com",
                "test",
                "password",
                1,
                1,
                LocalDate.now(),
                "test.jpg",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PostDto existingPostDto = PostDto.of(
                1L,
                "title",
                "content",
                0,
                0,
                0,
                "image.jpg",
                1,
                userDto,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(postRepository.findById(any())).thenReturn(Optional.of(existingPostDto.toEntity()));

        // when
        PostDto resultPostDto = postService.getPostDtoById(1L);

        // then
        assertNotNull(resultPostDto);
        assertEquals(existingPostDto, resultPostDto);
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회하면 PostErrorCode.NOT_FOUND 예외를 던진다.")
    void when_RetrieveNonExistingPost_Then_ThrowNotFoundException() {
        // given
        UserDto userDto = UserDto.of(
                1L,
                "test@test.com",
                "test",
                "password",
                1,
                1,
                LocalDate.now(),
                "test.jpg",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PostDto existingPostDto = PostDto.of(
                1L,
                "title",
                "content",
                0,
                0,
                0,
                "image.jpg",
                1,
                userDto,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(postRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> postService.getPostDtoById(1000L)
        );

        assertEquals(PostErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("게시글이 성공적으로 삭제되어야 한다.")
    void should_PostDeleteSuccessfully() {
        // given
        UserDto userDto = UserDto.of(
                1L,
                "test@test.com",
                "test",
                "password",
                1,
                1,
                LocalDate.now(),
                "test.jpg",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PostDto postDto = PostDto.of(
                1L,
                "title",
                "content",
                0,
                0,
                0,
                "image.jpg",
                1,
                userDto,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        postService.deletePost(postDto);

        // then
        verify(postRepository).delete(postDto.toEntity());
    }

    @Test
    @DisplayName("커서와 카테고리를 입력받아 카테고리 내 게시글을 paging 처리하여 보여준다.")
    void when_InputCursorAndCategory_Then_ShowPostOfCategoryAsPaging() {
        // given
        int category = 1;
        Long cursor = 2L;
        Pageable pageable = PageRequest.of(0, 10);

        UserDto userDto = UserDto.of(
                1L,
                "test@test.com",
                "test",
                "password",
                1,
                1,
                LocalDate.now(),
                "test.jpg",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<PostDto> postDtos = Arrays.asList(
                PostDto.of(
                        3L,
                        "title",
                        "content",
                        0,
                        0,
                        0,
                        "image.jpg",
                        1,
                        userDto,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ),
                PostDto.of(
                        4L,
                        "title",
                        "content",
                        0,
                        0,
                        0,
                        "image.jpg",
                        1,
                        userDto,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );

        List<Post> posts = Arrays.asList(
                Post.of(
                        3L,
                        "title",
                        "content",
                        0,
                        0,
                        0,
                        "image.jpg",
                        1,
                        userDto.toEntity(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ),
                Post.of(
                        4L,
                        "title",
                        "content",
                        0,
                        0,
                        0,
                        "image.jpg",
                        1,
                        userDto.toEntity(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );

        when(postRepository.findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable)).thenReturn(posts);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosByCategory(category, cursor, PageRequest.of(0, 10));

        // then
        verify(postRepository).findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable);
        assertEquals(postDtos.size(), resultPostDtos.size());
    }

    @Test
    @DisplayName("커서와 검색어를 입력받아 제목이나 본문이 검색어를 포함하는 게시글을 보여준다.")
    void when_InputCursorAndSearchWord_Then_ShowPostThatContainTitleOrContent() {
        // given
        Long cursor = 2L;
        String searchWord = "title";
        Pageable pageable = PageRequest.of(0, 10);

        UserDto userDto = UserDto.of(
                1L,
                "test@test.com",
                "test",
                "password",
                1,
                1,
                LocalDate.now(),
                "test.jpg",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<PostDto> postDtos = Arrays.asList(
                PostDto.of(
                        3L,
                        "title",
                        "content",
                        0,
                        0,
                        0,
                        "image.jpg",
                        1,
                        userDto,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ),
                PostDto.of(
                        4L,
                        "title",
                        "content",
                        0,
                        0,
                        0,
                        "image.jpg",
                        1,
                        userDto,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );

        List<Post> posts = Arrays.asList(
                Post.of(
                        3L,
                        "title",
                        "content",
                        0,
                        0,
                        0,
                        "image.jpg",
                        1,
                        userDto.toEntity(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ),
                Post.of(
                        4L,
                        "title",
                        "content",
                        0,
                        0,
                        0,
                        "image.jpg",
                        1,
                        userDto.toEntity(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );

        when(postRepository.findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(cursor, searchWord, cursor, searchWord, pageable)).thenReturn(posts);

        // when
        List<PostDto> resultPostDtos = postService.getPostDtosBySearchWord(searchWord, cursor, PageRequest.of(0, 10));

        // then
        verify(postRepository).findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(cursor, searchWord, cursor, searchWord, pageable);
        assertEquals(postDtos.size(), resultPostDtos.size());
    }
}