package org.orury.domain.post.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.PostErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.PostReader;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.post.domain.entity.PostLikePK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostReaderImpl implements PostReader {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    public List<Post> findByCategoryOrderByIdDesc(int category, Long cursor, Pageable pageable) {
        return (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByCategoryOrderByIdDesc(category, pageable)
                : postRepository.findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable);
    }

    @Override
    public List<Post> findByTitleContainingOrContentContainingOrderByIdDesc(String searchWord, Long cursor, Pageable pageable) {
        return (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, searchWord, pageable)
                : postRepository.findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(cursor, searchWord, cursor, searchWord, pageable);
    }

    @Override
    public List<Post> findByUserIdOrderByIdDesc(Long userId, Long cursor, Pageable pageable) {
        return (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByUserIdOrderByIdDesc(userId, pageable)
                : postRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
    }

    @Override
    public Page<Post> findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(int likeCount, LocalDateTime localDateTime, Pageable pageable) {
        return postRepository.findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc
                (NumberConstants.HOT_POSTS_BOUNDARY, LocalDateTime.now()
                        .minusMonths(1L), pageable);
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
    }

    @Override
    public boolean isPostLiked(Long userId, Long postId) {
        return postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId);
    }

    @Override
    public boolean existsByPostLikePK(PostLikePK postLikePK) {
        return postLikeRepository.existsByPostLikePK(postLikePK);
    }
}
