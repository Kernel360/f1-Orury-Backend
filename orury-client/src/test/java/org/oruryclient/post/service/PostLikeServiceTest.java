package org.oruryclient.post.service;

import org.orurycommon.error.exception.BusinessException;
import org.orurydomain.global.constants.NumberConstants;
import org.orurydomain.post.db.model.Post;
import org.orurydomain.post.db.model.PostLikePK;
import org.orurydomain.post.db.repository.PostLikeRepository;
import org.orurydomain.post.db.repository.PostRepository;
import org.orurydomain.post.dto.PostLikeDto;
import org.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] 게시글 좋아요 테스트")
@ActiveProfiles("test")
class PostLikeServiceTest {
    private PostLikeRepository postLikeRepository;
    private PostRepository postRepository;
    private PostLikeService postLikeService;

    @BeforeEach
    void setUp() {
        postLikeRepository = mock(PostLikeRepository.class);
        postRepository = mock(PostRepository.class);
        postLikeService = new PostLikeServiceImpl(postLikeRepository, postRepository);
    }

    @Test
    @DisplayName("유저가 게시글에 좋아요를 누르면 게시물에 좋아요 개수가 증가하고 좋아요 테이블에 데이터가 생성")
    void when_UserPostLike_Then_CreatePostLikeAndIncreasePostLikeCount() {
        // given
        PostLikePK postLikePK = createPostLikePK();
        PostLikeDto postLike = createPostLike(postLikePK);
        Post post = createPost();

        when(postRepository.findById(postLikePK.getPostId())).thenReturn(Optional.of(post));

        // when
        postLikeService.createPostLike(postLike);

        // then
        verify(postLikeRepository, times(1)).save(postLike.toEntity());
        verify(postRepository, times(1)).increaseLikeCount(postLikePK.getPostId());
    }

    @Test
    @DisplayName("좋아요시 게시글이 존재하지 않으면 NOT_FOUND 예외 발생")
    void verify_UserPostLikeIncreaseNotExistPost_Then_ExceptionNotFound() {
        // given
        PostLikePK postLikePK = createPostLikePK();
        PostLikeDto postLike = createPostLike(postLikePK);

        // when & then
        assertThrows(BusinessException.class, () -> postLikeService.createPostLike(postLike));
    }

    @Test
    @DisplayName("좋아요 취소시 게시글이 존재하지 않으면 NOT_FOUND 예외 발생")
    void verify_UserPostLikeDecreaseNotExistPost_Then_ExceptionNotFound() {
        // given
        PostLikePK postLikePK = createPostLikePK();
        PostLikeDto postLike = createPostLike(postLikePK);

        // when & then
        assertThrows(BusinessException.class, () -> postLikeService.deletePostLike(postLike));
    }

    @Test
    @DisplayName("유저가 게시글에 좋아요를 누르면 게시물에 좋아요 개수가 감소하고 좋아요 테이블에 데이터가 삭제")
    void when_UserPostLike_Then_DeletePostLikeAndDecreasePostLikeCount() {
        // given
        PostLikePK postLikePK = createPostLikePK();
        PostLikeDto postLike = createPostLike(postLikePK);
        Post post = createPost();

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postLikeRepository.existsByPostLikePK(any(PostLikePK.class))).thenReturn(true);

        // when
        postLikeService.deletePostLike(postLike);

        // then
        verify(postLikeRepository, times(1)).delete(postLike.toEntity());
        verify(postRepository, times(1)).decreaseLikeCount(postLikePK.getPostId());
    }

    @Test
    @DisplayName("유저가 해당 게시물에 좋아요를 눌렀는지 확인 -> 좋아요")
    void verify_UserIsLikedPost() {
        // given
        Long userId = 1L;
        Long postId = 1L;

        when(postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId)).thenReturn(true);

        // when
        postLikeService.isLiked(userId, postId);

        // then
        verify(postLikeRepository, times(1)).existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId);
    }

    private static Post createPost() {
        return Post.of(
                1L,
                "title",
                "content",
                1,
                1,
                1,
                List.of(),
                1,
                createUser().toEntity(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static UserDto createUser() {
        return UserDto.of(
                1L,
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

    private static PostLikeDto createPostLike(PostLikePK postLikePK) {
        return PostLikeDto.of(postLikePK);
    }

    private static PostLikePK createPostLikePK() {
        return PostLikePK.of(1L, 1L);
    }
}