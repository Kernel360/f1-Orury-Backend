package org.orury.client.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.post.infrastructure.PostLikeRepository;
import org.orury.domain.post.PostReader;
import org.orury.domain.post.PostStore;
import org.orury.domain.post.dto.PostLikeDto;
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
