package org.orury.admin.post.application;

import org.orury.domain.post.domain.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto getPost(Long postId);

    List<PostDto> getPosts();

    void deletePost(PostDto postDto);
}
