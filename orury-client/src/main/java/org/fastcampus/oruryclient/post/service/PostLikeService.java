package org.fastcampus.oruryclient.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurycommon.error.code.PostErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.post.db.repository.PostLikeRepository;
import org.fastcampus.orurydomain.post.db.repository.PostRepository;
import org.fastcampus.orurydomain.post.dto.PostLikeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createPostLike(PostLikeDto postLikeDto) {
        postRepository.findById(postLikeDto.postLikePK().getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
        if (postLikeRepository.existsByPostLikePK(postLikeDto.postLikePK())) return;

        postLikeRepository.save(postLikeDto.toEntity());
        postRepository.increaseLikeCount(postLikeDto.postLikePK().getPostId());
    }

    @Transactional
    public void deletePostLike(PostLikeDto postLikeDto) {
        postRepository.findById(postLikeDto.postLikePK().getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
        if (!postLikeRepository.existsByPostLikePK(postLikeDto.postLikePK())) return;

        postLikeRepository.delete(postLikeDto.toEntity());
        postRepository.decreaseLikeCount(postLikeDto.postLikePK().getPostId());
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long postId) {
        return postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId);
    }
}
