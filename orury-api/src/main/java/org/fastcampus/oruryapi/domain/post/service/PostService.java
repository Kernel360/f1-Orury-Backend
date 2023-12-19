package org.fastcampus.oruryapi.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.comment.db.repository.CommentRepository;
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
import org.fastcampus.oruryapi.domain.user.db.repository.UserRepository;
import org.fastcampus.oruryapi.global.constants.NumberConstants;
import org.fastcampus.oruryapi.global.error.BusinessException;
import org.fastcampus.oruryapi.domain.post.error.PostErrorCode;
import org.fastcampus.oruryapi.global.error.code.UserErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createPost(PostCreateRequest request) {
        UserDto userDto = UserDto.from(userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND)));
        Post post = request.toDto(userDto).toEntity();

        postRepository.save(post);
    }

    public PostResponse getPostById(Long id) {
        PostDto postDto = PostDto.from(postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND)));
        int likeCount = postLikeRepository.countByPostLikePK_PostId(postDto.id());
        int commentCount = commentRepository.countByPostId(postDto.id());
        boolean isLike = postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(NumberConstants.USER_ID, postDto.id());

        return PostResponse.of(postDto, likeCount, commentCount, isLike);
    }

    public PostsWithCursorResponse getPostsByCategory(int category, Long cursor, Pageable pageable) {
        List<Post> posts = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByCategoryOrderByIdDesc(category, pageable)
                : postRepository.findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable);

        return getPostsWithCursorResponse(posts);
    }

    public PostsWithCursorResponse getPostsBySearchWord(String searchWord, Long cursor, Pageable pageable) {
        List<Post> posts = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, searchWord, pageable)
                : postRepository.findByTitleContainingOrContentContainingAndIdLessThanOrderByIdDesc(searchWord, searchWord, cursor, pageable);

        return getPostsWithCursorResponse(posts);
    }

    @Transactional
    public void updatePost(PostUpdateRequest request) {
        PostDto postDto = PostDto.from(postRepository.findById(request.id())
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND)));
        UserDto userDto = UserDto.from(userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND)));

        postRepository.save(request.toDto(postDto, userDto).toEntity());
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                        .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));

        postRepository.delete(post);
    }

    @Transactional
    public void addViewCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
        PostDto postDto = PostDto.of(post.getId(), post.getTitle(), post.getContent(),
                post.getViewCount() + 1,
                post.getImages(), post.getCategory(), UserDto.from(post.getUser()), post.getCreatedAt(), post.getUpdatedAt());

        postRepository.save(postDto.toEntity());
    }

    private PostsWithCursorResponse getPostsWithCursorResponse(List<Post> posts) {
        List<PostsResponse> postsResponses = posts.stream()
                .map(post -> {
                    PostDto postDto = PostDto.from(post);
                    int likeCount = postLikeRepository.countByPostLikePK_PostId(postDto.id());
                    int commentCount = commentRepository.countByPostId(postDto.id());

                    return PostsResponse.of(postDto, likeCount, commentCount);
                }).toList();

        return PostsWithCursorResponse.of(postsResponses);
    }
}
