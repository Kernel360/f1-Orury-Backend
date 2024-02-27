package org.orury.client.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.PostErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.domain.ImageUtils;
import org.orury.domain.post.domain.db.Post;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.infrastructure.PostRepository;
import org.orury.domain.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final ImageUtils imageUtils;

    @Transactional
    public void createPost(PostDto postDto, List<MultipartFile> images) {
        imageUploadAndSave(postDto, images);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostDtosByCategory(int category, Long cursor, Pageable pageable) {
        List<Post> posts = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByCategoryOrderByIdDesc(category, pageable)
                : postRepository.findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable);
        return transferPosts(posts);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostDtosBySearchWord(String searchWord, Long cursor, Pageable pageable) {
        List<Post> posts = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, searchWord, pageable)
                : postRepository.findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(cursor, searchWord, cursor, searchWord, pageable);

        return transferPosts(posts);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostDtosByUserId(Long userId, Long cursor, Pageable pageable) {
        List<Post> posts = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByUserIdOrderByIdDesc(userId, pageable)
                : postRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);

        return transferPosts(posts);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getHotPostDtos(Pageable pageable) {
        Page<Post> posts = postRepository.findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc
                (NumberConstants.HOT_POSTS_BOUNDARY, LocalDateTime.now()
                        .minusMonths(1L), pageable);
        return transferPosts(posts);
    }

    @Transactional
    public void updatePost(PostDto postDto, List<MultipartFile> images) {
        imageUtils.oldS3ImagesDelete(S3Folder.POST.getName(), postDto.images());
        imageUploadAndSave(postDto, images);
    }

    @Transactional
    public void deletePost(PostDto postDto) {
        imageUtils.oldS3ImagesDelete(S3Folder.POST.getName(), postDto.images());
        postRepository.delete(postDto.toEntity());
    }

    @Transactional
    public void addViewCount(PostDto postDto) {
        postRepository.updateViewCount(postDto.id());
    }

    @Transactional(readOnly = true)
    public PostDto getPostDtoById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));

        return transferPosts(List.of(post)).get(0);
    }

    public void isValidate(PostDto postDto, UserDto userDto) {
        if (!Objects.equals(postDto.userDto()
                .id(), userDto.id()))
            throw new BusinessException(PostErrorCode.FORBIDDEN);
    }

    public int getNextPage(Page<PostDto> postDtos, int page) {
        return (postDtos.hasNext())
                ? page + 1
                : NumberConstants.LAST_PAGE;
    }

    private void imageUploadAndSave(PostDto postDto, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            postRepository.save(postDto.toEntity(List.of()));
        } else {
            List<String> images = imageUtils.upload(S3Folder.POST.getName(), files);
            postRepository.save(postDto.toEntity(images));
        }
    }

    private Page<PostDto> transferPosts(Page<Post> posts) {
        return posts.map(post -> {
            var images = imageUtils.getUrls(S3Folder.POST.getName(), post.getImages());
            var postUserImage = imageUtils.getUrls(S3Folder.USER.getName(), List.of(post.getUser()
                            .getProfileImage()))
                    .get(0);
            return PostDto.from(post, images, postUserImage);
        });
    }

    private List<PostDto> transferPosts(List<Post> posts) {
        return posts.stream()
                .map(post -> {
                    var images = imageUtils.getUrls(S3Folder.POST.getName(), post.getImages());
                    var postUserImage = imageUtils.getUserImageUrl(post.getUser()
                            .getProfileImage());
                    return PostDto.from(post, images, postUserImage);
                })
                .toList();
    }
}
