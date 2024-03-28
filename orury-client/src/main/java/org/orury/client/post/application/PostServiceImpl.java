package org.orury.client.post.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.PostErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.image.domain.ImageStore;
import org.orury.domain.post.domain.PostReader;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.domain.dto.PostLikeDto;
import org.orury.domain.post.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.orury.common.util.S3Folder.POST;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {
    private final PostReader postReader;
    private final PostStore postStore;
    private final ImageStore imageStore;

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostDtosByCategory(int category, Long cursor, Pageable pageable) {
        return postReader.findByCategoryOrderByIdDesc(category, cursor, pageable).stream()
                .map(this::postDtoConverter)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostDtosBySearchWord(String searchWord, Long cursor, Pageable pageable) {
        return postReader.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, cursor, pageable).stream()
                .map(this::postDtoConverter)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPostDtosByUserId(Long userId, Long cursor, Pageable pageable) {
        return postReader.findByUserIdOrderByIdDesc(userId, cursor, pageable).stream()
                .map(this::postDtoConverter)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto> getHotPostDtos(Pageable pageable) {
        return postReader.findByLikeCountGreaterDescAndCreatedAtDesc(pageable)
                .map(this::postDtoConverter);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostDtoById(Long id) {
        var post = postReader.findById(id)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
        return postDtoConverter(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostDtoById(Long userId, Long postId) {
        var post = postReader.findById(postId)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
        var postUser = post.getUser().getId();
        if (!postUser.equals(userId))
            throw new BusinessException(PostErrorCode.FORBIDDEN);
        return postDtoConverter(post);
    }

    @Override
    public void createPost(PostDto postDto, List<MultipartFile> files) {
        var newImages = imageStore.upload(POST, files);
        imageStore.delete(POST, postDto.images());
        postStore.save(postDto.toEntity(newImages));
    }

    @Override
    public void deletePost(PostDto postDto) {
        imageStore.delete(POST, postDto.images());
        postStore.delete(postDto.toEntity());
    }

    @Override
    public int getNextPage(Page<PostDto> postDtos, int page) {
        return (postDtos.hasNext()) ? page + 1 : NumberConstants.LAST_PAGE;
    }

    @Override
    public void createPostLike(PostLikeDto postLikeDto) {
        if (postReader.existsByPostLikePK(postLikeDto.postLikePK())) return;
        postStore.save(postLikeDto.toEntity());
    }

    @Override
    public void deletePostLike(PostLikeDto postLikeDto) {
        if (!postReader.existsByPostLikePK(postLikeDto.postLikePK())) return;
        postStore.delete(postLikeDto.toEntity());
    }

    @Override
    public void updateViewCount(Long id) {
        postStore.updateViewCount(id);
    }

    private PostDto postDtoConverter(Post post) {
        var isLike = postReader.isPostLiked(post.getUser().getId(), post.getId());
        return PostDto.from(post, isLike);
    }
}
