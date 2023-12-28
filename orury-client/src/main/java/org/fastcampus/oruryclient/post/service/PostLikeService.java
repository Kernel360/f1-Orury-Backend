package org.fastcampus.oruryclient.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.domain.post.converter.dto.PostLikeDto;
import org.fastcampus.oruryclient.domain.post.db.repository.PostLikeRepository;
import org.fastcampus.oruryclient.domain.post.db.repository.PostRepository;
import org.fastcampus.oruryclient.domain.post.error.PostErrorCode;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public void createPostLike(PostLikeDto postLikeDto) {
        postRepository.findById(postLikeDto.postLikePK().getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));

        postLikeRepository.save(postLikeDto.toEntity());
        postRepository.increaseLikeCount(postLikeDto.postLikePK().getPostId());
    }

    public void deletePostLike(PostLikeDto postLikeDto) {
        postRepository.findById(postLikeDto.postLikePK().getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
        if (!postLikeRepository.existsByPostLikePK(postLikeDto.postLikePK())) return;

        postLikeRepository.delete(postLikeDto.toEntity());
        postRepository.decreaseLikeCount(postLikeDto.postLikePK().getPostId());
    }

    public boolean isLiked(Long userId, Long postId){
        return postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId);
    }
}
