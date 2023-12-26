package org.fastcampus.oruryapi.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostLikeDto;
import org.fastcampus.oruryapi.domain.post.converter.request.PostLikeRequest;
import org.fastcampus.oruryapi.domain.post.db.model.PostLike;
import org.fastcampus.oruryapi.domain.post.db.repository.PostLikeRepository;
import org.fastcampus.oruryapi.domain.post.db.repository.PostRepository;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.db.repository.UserRepository;
import org.fastcampus.oruryapi.global.error.BusinessException;
import org.fastcampus.oruryapi.domain.post.error.PostErrorCode;
import org.fastcampus.oruryapi.global.error.code.UserErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void createPostLike(PostLikeRequest postLikeRequest) {
        UserDto userDto = UserDto.from(userRepository.findById(postLikeRequest.userId())
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND)));
        PostDto postDto = PostDto.from(postRepository.findById(postLikeRequest.postId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND)));
        PostLike postLike = PostLikeDto.from(PostLike.of(postLikeRequest.toDto(userDto, postDto).toEntity())).toEntity();

        postLikeRepository.save(postLike);
    }

    public void deletePostLike(PostLikeRequest postLikeRequest) {
        UserDto userDto = UserDto.from(userRepository.findById(postLikeRequest.userId())
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND)));
        PostDto postDto = PostDto.from(postRepository.findById(postLikeRequest.postId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND)));
        Optional<PostLike> postLike = postLikeRepository.findByPostLikePK_UserIdAndPostLikePK_PostId(userDto.id(), postDto.id());
        if (postLike.isEmpty()) return;

        postLikeRepository.delete(postLike.get());
    }

    public boolean isLiked(Long userId, Long postId){
        return postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId);
    }

    public void isValidate(Long userId, Long postId){
        postRepository.findById(postId)
                .ifPresentOrElse(
                        post -> {},
                        () -> {throw new BusinessException(PostErrorCode.NOT_FOUND);}
                );
        userRepository.findById(userId)
                .ifPresentOrElse(
                        user -> {},
                        () -> {throw new BusinessException(PostErrorCode.NOT_FOUND);}

                );
    }
}
