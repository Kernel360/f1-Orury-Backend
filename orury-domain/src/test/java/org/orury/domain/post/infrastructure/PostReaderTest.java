package org.orury.domain.post.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.DomainFixtureFactory;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.entity.Post;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("[Reader] PostReader 테스트")
class PostReaderTest extends InfrastructureTest {

    private final Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
    private final Post expectedPost = DomainFixtureFactory.TestPost.builder().id(1L).build().get();
    private final List<Post> expectedPosts = IntStream.rangeClosed(1, 10)
            .mapToObj(idx -> DomainFixtureFactory.TestPost.builder().id((long) idx).build().get())
            .toList();

    @DisplayName("카테고리로 첫 번째 커서로 게시글 목록 조회 - 성공")
    @Test
    void when_FindByCategoryOrderByIdDescWithFirstCursor_Then_Success() {
        // given
        int category = 1;
        Long cursor = NumberConstants.FIRST_CURSOR;
        given(postRepository.findByCategoryOrderByIdDesc(category, pageable)).willReturn(expectedPosts);

        // when
        List<Post> result = postReader.findByCategoryOrderByIdDesc(category, cursor, pageable);

        // then
        assertSame(expectedPosts, result);
        verify(postRepository, times(1)).findByCategoryOrderByIdDesc(category, pageable);
    }

    @DisplayName("카테고리로 첫 번째가 아닌 커서로 게시글 목록 조회 - 성공")
    @Test
    void when_FindByCategoryOrderByIdDescWithNonFirstCursor_Then_Success() {
        // given
        int category = 1;
        Long cursor = 10L; // A cursor that is not the first, indicating pagination.
        given(postRepository.findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable)).willReturn(expectedPosts);

        // when
        List<Post> result = postReader.findByCategoryOrderByIdDesc(category, cursor, pageable);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertSame(expectedPosts, result);
        assertEquals(10, result.size());
    }

    @DisplayName("제목 또는 내용에 검색어가 포함된 게시글과 첫번째 커서로 목록 조회 - 성공")
    @Test
    void when_FindByTitleContainingOrContentContainingOrderByIdDescWithFirstCursor_Then_Success() {
        // given
        String searchWord = "test";
        Long cursor = NumberConstants.FIRST_CURSOR;
        given(postRepository.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, searchWord, pageable)).willReturn(expectedPosts);

        // when
        List<Post> result = postReader.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, cursor, pageable);

        // then
        assertSame(expectedPosts, result);
        verify(postRepository, times(1)).findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, searchWord, pageable);
    }

    @DisplayName("제목 또는 내용에 검색어가 포함된 게시글과 첫번째 아닌 커서로 목록 조회 - 성공")
    @Test
    void when_FindByTitleContainingOrContentContainingOrderByIdDescWithNonFirstCursor_Then_Success() {
        // given
        String searchWord = "test";
        Long cursor = 10L;
        given(postRepository.findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(cursor, searchWord, cursor, searchWord, pageable)).willReturn(expectedPosts);

        // when
        List<Post> result = postReader.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, cursor, pageable);

        // then
        assertSame(expectedPosts, result);
        verify(postRepository, times(1)).findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(cursor, searchWord, cursor, searchWord, pageable);
    }

    @DisplayName("유저 id로 유저가 작성한 가장 최근 게시글 목록 조회 - 성공")
    @Test
    void when_FindByUserIdOrderByIdDesc_Then_Success() {
        // given
        Long userId = 1L;
        Long cursor = NumberConstants.FIRST_CURSOR;
        given(postRepository.findByUserIdOrderByIdDesc(userId, pageable)).willReturn(expectedPosts);

        // when
        List<Post> result = postReader.findByUserIdOrderByIdDesc(userId, cursor, pageable);

        // then
        assertSame(expectedPosts, result);
        verify(postRepository, times(1)).findByUserIdOrderByIdDesc(userId, pageable);
    }

    @DisplayName("유저 id로 유저가 작성한 게시글 목록 조회 - 성공")
    @Test
    void when_FindByUserIdAndIdLessThanOrderByIdDesc_Then_Success() {
        // given
        Long userId = 1L;
        Long cursor = 10L;
        given(postRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable)).willReturn(expectedPosts);

        // when
        List<Post> result = postReader.findByUserIdOrderByIdDesc(userId, cursor, pageable);

        // then
        assertSame(expectedPosts, result);
        verify(postRepository, times(1)).findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
    }

    @DisplayName("좋아요 수가 특정 수 이상이며, 특정 시간 이후에 작성된 게시글 목록 조회 - 성공")
    @Test
    void when_FindByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc_Then_Success() {
        // given
        var expectedPage = new PageImpl<>(expectedPosts, pageable, expectedPosts.size());

        given(postRepository.findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(
                anyInt(), any(), any())).willReturn(expectedPage);

        // when
        var resultPage = postReader.findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(pageable);

        // then
        assertNotNull(resultPage);
        assertEquals(expectedPage.getTotalElements(), resultPage.getTotalElements());
        verify(postRepository, times(1)).findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(
                anyInt(), any(), any());
    }

    @DisplayName("게시글 id로 게시글 조회 - 성공")
    @Test
    void when_FindById_Then_Success() {
        // given
        Long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.of(expectedPost));

        // when
        var result = postReader.findById(postId);

        // then
        assertTrue(result.isPresent());
        assertEquals(postId, result.get().getId());
        verify(postRepository, times(1)).findById(postId);
    }

    @DisplayName("게시글 id로 게시글 조회 - null 반환")
    @Test
    void when_FindById_Then_ReturnNull() {
        // given
        Long postId = 1L;
        var expected = Optional.empty();

        // when
        var result = postReader.findById(postId);

        // then
        assertFalse(result.isPresent());
        assertEquals(expected, result);
        verify(postRepository, times(1)).findById(postId);
    }

    @DisplayName("유저 아이디와 게시글 아이디로 좋아요 여부 조회시 True 반환 - 성공")
    @Test
    void given_UserIdAndPostId_WhenIsPostLiked_Then_ReturnTrue() {
        // given
        Long userId = 1L;
        Long postId = 1L;
        given(postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId)).willReturn(true);

        // when
        boolean isLiked = postReader.isPostLiked(userId, postId);

        // then
        assertTrue(isLiked);
        verify(postLikeRepository).existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId);
    }

    @DisplayName("게시글 전부 조회 - 성공")
    @Test
    void when_FindAll_Then_Success() {
        // given
        given(postRepository.findAll()).willReturn(expectedPosts);

        // when
        var result = postReader.findAll();

        // then
        assertSame(expectedPosts, result);
        verify(postRepository, times(1)).findAll();
    }
}