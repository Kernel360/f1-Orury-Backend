package org.orury.client.post.application;

import static org.orury.domain.global.constants.NumberConstants.POST_PAGINATION_SIZE;

import org.orury.client.post.interfaces.request.PostRequest;
import org.orury.client.post.interfaces.response.PostResponse;
import org.orury.client.post.interfaces.response.PostsResponse;
import org.orury.client.post.interfaces.response.PostsWithPageResponse;
import org.orury.common.error.code.PostErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.post.domain.PostService;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.domain.dto.PostLikeDto;
import org.orury.domain.post.domain.entity.PostLikePK;
import org.orury.domain.user.domain.UserService;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostFacade {
    private final PostService postService;
    private final UserService userService;

    public void createPost(Long userId, PostRequest request, List<MultipartFile> files) {
        var user = userService.getUserDtoById(userId);
        var post = request.toDto(user);
        postService.createPost(post, files);
    }

    public void updatePost(Long userId, PostRequest request, List<MultipartFile> files) {
        var user = userService.getUserDtoById(userId);
        var oldPost = postService.getPostDtoById(request.id());
        isValidate(oldPost, userId);
        var newPost = request.toDto(oldPost, user);
        postService.createPost(newPost, files);
    }

    public void deletePost(Long userId, Long postId) {
        var post = postService.getPostDtoById(postId);
        isValidate(post, userId);
        postService.deletePost(post);
    }

    public PostResponse getPostById(Long userId, Long postId) {
        PostDto post = postService.getPostDtoById(postId);
        UserDto user = userService.getUserDtoById(userId);
        postService.updateViewCount(postId);
        return PostResponse.of(post, user);
    }

    public List<PostsResponse> getPostsByCategory(int category, Long cursor, Pageable pageable) {
        return postService.getPostDtosByCategory(category, cursor, pageable)
                .stream()
                .map(PostsResponse::of)
                .toList();
    }

    public List<PostsResponse> getPostsBySearchWord(String searchWord, Long cursor) {
        var pageRequest = PageRequest.of(0, POST_PAGINATION_SIZE);
        return postService.getPostDtosBySearchWord(searchWord, cursor, pageRequest)
                .stream()
                .map(PostsResponse::of)
                .toList();
    }

    public PostsWithPageResponse getHotPosts(int page) {
        var pageRequest = PageRequest.of(page, POST_PAGINATION_SIZE);
        var post = postService.getHotPostDtos(pageRequest);
        int nextPage = postService.getNextPage(post, page);
        return PostsWithPageResponse.of(post.stream().map(PostsResponse::of).toList(), nextPage);
    }

    public void createPostLike(Long userId, Long postId) {
        var postLikeDto = PostLikeDto.of(PostLikePK.of(userId, postId));
        postService.createPostLike(postLikeDto);
    }

    public void deletePostLike(Long userId, Long postId) {
        var postLikeDto = PostLikeDto.of(PostLikePK.of(userId, postId));
        postService.deletePostLike(postLikeDto);
    }

    private void isValidate(PostDto postDto, Long userId) {
        if (postDto.userDto().id().equals(userId))
            throw new BusinessException(PostErrorCode.FORBIDDEN);
    }
}
