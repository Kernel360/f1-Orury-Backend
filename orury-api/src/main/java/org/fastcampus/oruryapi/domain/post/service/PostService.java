package org.fastcampus.oruryapi.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.comment.db.repository.CommentRepository;
import org.fastcampus.oruryapi.domain.comment.error.CommentErrorCode;
import org.fastcampus.oruryapi.domain.post.converter.dto.PostDto;
import org.fastcampus.oruryapi.domain.post.converter.request.PostCreateRequest;
import org.fastcampus.oruryapi.domain.post.converter.request.PostUpdateRequest;
import org.fastcampus.oruryapi.domain.post.converter.response.PostResponse;
import org.fastcampus.oruryapi.domain.post.converter.response.PostsResponse;
import org.fastcampus.oruryapi.domain.post.converter.response.PostsWithCursorResponse;
import org.fastcampus.oruryapi.domain.post.db.model.Post;
import org.fastcampus.oruryapi.domain.post.db.repository.PostLikeRepository;
import org.fastcampus.oruryapi.domain.post.db.repository.PostRepository;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.fastcampus.oruryapi.domain.user.db.repository.UserRepository;
import org.fastcampus.oruryapi.global.constants.NumberConstants;
import org.fastcampus.oruryapi.global.error.BusinessException;
import org.fastcampus.oruryapi.domain.post.error.PostErrorCode;
import org.fastcampus.oruryapi.global.error.code.UserErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createPost(PostDto postDto) {
        postRepository.save(postDto.toEntity());
    }

    public List<PostDto> getPostDtosByCategory(int category, Long cursor, Pageable pageable) {
        List<Post> posts = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByCategoryOrderByIdDesc(category, pageable)
                : postRepository.findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable);

        return posts.stream()
                .map(PostDto::from).toList();
    }

    public List<PostDto> getPostDtosBySearchWord(String searchWord, Long cursor, Pageable pageable) {
        List<Post> posts = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, searchWord, pageable)
                : postRepository.findByTitleContainingOrContentContainingAndIdLessThanOrderByIdDesc(searchWord, searchWord, cursor, pageable);

        return posts.stream()
                .map(PostDto::from).toList();
    }

    @Transactional
    public void updatePost(PostDto postDto) {
        postRepository.save(postDto.toEntity());
    }

    @Transactional
    public void deletePost(PostDto postDto) {
        postRepository.delete(postDto.toEntity());
    }

    @Transactional
    public void addViewCount(PostDto postDto) {
        postRepository.updateViewCount(postDto.id());
    }

    public PostDto getPostDtoById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new BusinessException(PostErrorCode.NOT_FOUND));
        return PostDto.from(post);
    }

    public void isValidate(PostDto postDto, UserDto userDto){
        if(!Objects.equals(postDto.userDto().id(), userDto.id()))
            throw new BusinessException(PostErrorCode.FORBIDDEN);
    }
}
