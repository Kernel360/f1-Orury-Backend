package org.oruryclient.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orurycommon.error.code.PostErrorCode;
import org.orurycommon.error.exception.BusinessException;
import org.orurydomain.post.db.repository.PostLikeRepository;
import org.orurydomain.post.db.repository.PostRepository;
import org.orurydomain.post.dto.PostLikeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Override
    public void createPostLike(PostLikeDto postLikeDto) {
        postRepository.findById(postLikeDto.postLikePK()
                        .getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
        if (postLikeRepository.existsByPostLikePK(postLikeDto.postLikePK())) return;

        postLikeRepository.save(postLikeDto.toEntity());
        postRepository.increaseLikeCount(postLikeDto.postLikePK()
                .getPostId());
    }

    @Override
    public void deletePostLike(PostLikeDto postLikeDto) {
        postRepository.findById(postLikeDto.postLikePK()
                        .getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
        if (!postLikeRepository.existsByPostLikePK(postLikeDto.postLikePK())) return;

        postLikeRepository.delete(postLikeDto.toEntity());
        postRepository.decreaseLikeCount(postLikeDto.postLikePK()
                .getPostId());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long postId) {
        return postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId);
    }
}
