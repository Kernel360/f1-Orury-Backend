package org.orury.client.post.application;

import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.domain.dto.PostLikeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<PostDto> getPostDtosByCategory(int category, Long cursor, Pageable pageable);

    List<PostDto> getPostDtosBySearchWord(String searchWord, Long cursor, Pageable pageable);

    List<PostDto> getPostDtosByUserId(Long userId, Long cursor, Pageable pageable);

    Page<PostDto> getHotPostDtos(Pageable pageable);

    PostDto getPostDtoById(Long postId);

    PostDto getPostDtoById(Long userId, Long postId);

    void createPost(PostDto postDto, List<MultipartFile> files);

    void deletePost(PostDto postDto);

    int getNextPage(Page<PostDto> postDtos, int page);

    void createPostLike(PostLikeDto postLikeDto);

    void deletePostLike(PostLikeDto postLikeDto);

    void updateViewCount(Long id);
}
