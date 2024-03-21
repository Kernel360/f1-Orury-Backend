package org.orury.domain.post.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.common.util.S3Folder;
import org.orury.domain.PostDomainFixture;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.post.domain.entity.PostLike;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("[Store] PostStore 테스트")
class PostStoreTest extends InfrastructureTest {
    private Post expectedPost;
    private PostLike expectedPostLike;

    @BeforeEach
    void setUps() {
        expectedPost = PostDomainFixture.TestPost.createPost().build().get();
        expectedPostLike = PostDomainFixture.TestPostLike.createPostLike().build().get();
    }

    @DisplayName("게시글 생성 - 성공")
    @Test
    void should_SavePost_Success() {
        // given
        given(postRepository.save(any(Post.class))).willReturn(expectedPost);
        // when
        postStore.save(expectedPost);
        // then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @DisplayName("게시글 삭제 - 성공")
    @Test
    void should_DeletePost_Success() {
        // given
        willDoNothing().given(postRepository).delete(expectedPost);
        // when
        postStore.delete(expectedPost);
        // then
        verify(postRepository, times(1)).delete(expectedPost);
    }

    @DisplayName("게시글 좋아요 생성 - 성공")
    @Test
    void should_SavePostLike_Success() {
        // given
        given(postLikeRepository.save(any(PostLike.class))).willReturn(expectedPostLike);
        willDoNothing().given(postRepository).increaseLikeCount(expectedPostLike.getPostLikePK().getPostId());
        // when
        postStore.save(expectedPostLike);
        // then
        verify(postLikeRepository, times(1)).save(any(PostLike.class));
    }

    @DisplayName("게시글 좋아요 삭제 - 성공")
    @Test
    void should_DeletePostLike_Success() {
        // given
        willDoNothing().given(postLikeRepository).delete(any(PostLike.class));
        willDoNothing().given(postRepository).decreaseLikeCount(expectedPostLike.getPostLikePK().getPostId());
        // when
        postStore.delete(expectedPostLike);
        // then
        verify(postLikeRepository, times(1)).delete(any(PostLike.class));
        verify(postRepository, times(1)).decreaseLikeCount(expectedPostLike.getPostLikePK().getPostId());
    }

    @DisplayName("게시글 조회수 증가 - 성공")
    @Test
    void should_UpdateViewCount_Success() {
        // given
        willDoNothing().given(postRepository).updateViewCount(expectedPost.getId());
        // when
        postStore.updateViewCount(expectedPost.getId());
        // then
        verify(postRepository, times(1)).updateViewCount(expectedPost.getId());
    }

    @DisplayName("유저 id와 관련된 게시글 좋아요 삭제 및 조회 수 감소 - 성공")
    @Test
    void should_DeletePostLikesByUserId_Success() {
        // given
        var expectedPostLikes = IntStream.range(1, 10)
                .mapToObj(i -> PostDomainFixture.TestPostLike.createPostLike((long) i, 1L).build().get()).toList();
        given(postLikeRepository.findByPostLikePK_UserId(expectedPostLike.getPostLikePK().getUserId()))
                .willReturn(expectedPostLikes);
        // when
        postStore.deletePostLikesByUserId(expectedPostLike.getPostLikePK().getUserId());

        // then
        verify(postLikeRepository, times(1)).findByPostLikePK_UserId(expectedPostLike.getPostLikePK().getUserId());
        verify(postLikeRepository, times(9)).delete(any(PostLike.class));
        verify(postRepository, times(9)).decreaseCommentCount(any(Long.class));
    }

    @DisplayName("유저 id와 관련된 게시글 삭제 - 성공")
    @Test
    void should_DeletePostsByUserId_Success() {
        // given
        var expectedPosts = IntStream.range(1, 10)
                .mapToObj(i -> PostDomainFixture.TestPost.createPost((long) i).build().get()).toList();
        given(postRepository.findByUserId(anyLong())).willReturn(expectedPosts);
        willDoNothing().given(imageStore).delete(eq(S3Folder.POST), eq(List.of()));
        willDoNothing().given(postRepository).delete(any(Post.class));

        // when
        postStore.deletePostsByUserId(expectedPosts.get(0).getUser().getId());

        // then
        verify(postRepository, times(1)).findByUserId(anyLong());
        verify(imageStore, times(9)).delete(eq(S3Folder.POST), eq(List.of()));
        verify(postRepository, times(9)).delete(any(Post.class));
    }
}