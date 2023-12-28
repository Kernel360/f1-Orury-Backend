package org.fastcampus.oruryclient.post.service;

import org.fastcampus.orurydomain.post.db.model.Post;
import org.fastcampus.orurydomain.post.db.model.PostLikePK;
import org.fastcampus.orurydomain.post.db.repository.PostLikeRepository;
import org.fastcampus.orurydomain.post.db.repository.PostRepository;
import org.fastcampus.orurydomain.post.dto.PostLikeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 좋아요 관련 테스트")
@ActiveProfiles("test")
class PostLikeServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostLikeRepository postLikeRepository;
    @InjectMocks
    private PostLikeService postLikeService;

    @DisplayName("유저가 게시글에 좋아요를 누르면 게시물에 좋아요 개수가 증가하고 좋아요 테이블에 데이터가 생성")
    @Test
    void should_CreatePostLikeAndIncreasePostLikeCount() {
        //given
        PostLikePK postLikePK = createPostLikePK();
        PostLikeDto postLike = createPostLike(postLikePK);
        Post post = createPost();

        //when
        when(postRepository.findById(postLikePK.getPostId())).thenReturn(Optional.of(post));

        postLikeService.createPostLike(postLike);

        //then
        verify(postLikeRepository).save(postLike.toEntity());
        verify(postRepository).increaseLikeCount(postLikePK.getPostId());
    }

    @DisplayName("유저가 게시글에 좋아요를 누르면 게시물에 좋아요 개수가 감소하고 좋아요 테이블에 데이터가 삭제")
    @Test
    void should_DeletePostLikeAndDecreasePostLikeCount() {
        //given
        PostLikePK postLikePK = createPostLikePK();
        PostLikeDto postLike = createPostLike(postLikePK);
        Post post = createPost();

        //when
        when(postRepository.findById(postLikePK.getPostId())).thenReturn(Optional.of(post));
        when(postLikeRepository.existsByPostLikePK(postLikePK)).thenReturn(true);

        postLikeService.deletePostLike(postLike);

        //then
        verify(postLikeRepository).delete(postLike.toEntity());
        verify(postRepository).decreaseLikeCount(postLikePK.getPostId());
    }

    @DisplayName("유저가 해당 게시물에 좋아요를 눌렀는지 확인 -> 좋아요")
    @Test
    void verify_UserIsLikedPost() {
        //given
        Long userId = 1L;
        Long postId = 1L;

        //when
        when(postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId)).thenReturn(true);
        postLikeService.isLiked(userId, postId);

        //then
        verify(postLikeRepository).existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId);
    }

    private static Post createPost() {
        return Post.of(
                1L,
                "title",
                "content",
                1,
                1,
                1,
                "image",
                1,
                any(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static PostLikeDto createPostLike(PostLikePK postLikePK) {
        return PostLikeDto.of(postLikePK);
    }

    private static PostLikePK createPostLikePK() {
        return PostLikePK.of(1L, 1L);
    }
}