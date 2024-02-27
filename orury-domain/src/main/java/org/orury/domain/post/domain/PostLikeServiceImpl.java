package org.orury.domain.post.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.post.domain.dto.PostLikeDto;
import org.orury.domain.post.infrastructure.PostLikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostStore postStore;
    private final PostReader postReader;

    @Override
    public void createPostLike(PostLikeDto postLikeDto) {
        postReader.findById(postLikeDto.postLikePK().getPostId());
        if (!postLikeRepository.existsByPostLikePK(postLikeDto.postLikePK())) return;
        postLikeRepository.save(postLikeDto.toEntity());
        postStore.increaseLikeCount(postLikeDto.postLikePK().getPostId());
    }

    @Override
    public void deletePostLike(PostLikeDto postLikeDto) {
        postReader.findById(postLikeDto.postLikePK().getPostId());
        if (!postLikeRepository.existsByPostLikePK(postLikeDto.postLikePK())) return;
        postLikeRepository.delete(postLikeDto.toEntity());
        postStore.decreaseLikeCount(postLikeDto.postLikePK().getPostId());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long postId) {
        return postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId);
    }

}
