package org.orury.admin.post.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.PostErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.global.image.ImageReader;
import org.orury.domain.global.image.ImageStore;
import org.orury.domain.post.domain.PostReader;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.domain.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.orury.common.util.S3Folder.POST;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {
    private final PostReader postReader;
    private final PostStore postStore;
    private final ImageReader imageReader;
    private final ImageStore imageStore;

    @Override
    @Transactional(readOnly = true)
    public PostDto getPost(Long postId) {
        var post = postReader.findById(postId)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
        return postImageConverter(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPosts() {
        return postReader.findAll().stream()
                .map(this::postImageConverter)
                .toList();
    }

    @Override
    public void deletePost(PostDto post) {
        imageStore.delete(POST, post.images());
        postStore.delete(post.toEntity());
    }

    private PostDto postImageConverter(Post post) {
        var links = imageReader.getImageLinks(POST, post.getImages());
        var profileLink = imageReader.getUserImageLink(post.getUser().getProfileImage());
        var isLike = postReader.isPostLiked(post.getUser().getId(), post.getId());
        return PostDto.from(post, links, profileLink, isLike);
    }
}
