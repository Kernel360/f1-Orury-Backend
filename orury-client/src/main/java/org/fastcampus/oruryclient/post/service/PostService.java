package org.fastcampus.oruryclient.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.oruryclient.global.logging.Logging;
import org.fastcampus.oruryclient.post.error.PostErrorCode;
import org.fastcampus.orurydomain.comment.db.model.Comment;
import org.fastcampus.orurydomain.comment.db.repository.CommentLikeRepository;
import org.fastcampus.orurydomain.comment.db.repository.CommentRepository;
import org.fastcampus.orurydomain.post.db.model.Post;
import org.fastcampus.orurydomain.post.db.repository.PostLikeRepository;
import org.fastcampus.orurydomain.post.db.repository.PostRepository;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Logging
    @Transactional
    public void createPost(PostDto postDto) {
        postRepository.save(postDto.toEntity());
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostDtosByCategory(int category, Long cursor, Pageable pageable) {
        List<Post> posts = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByCategoryOrderByIdDesc(category, pageable)
                : postRepository.findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable);

        return posts.stream()
                .map(PostDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostDtosBySearchWord(String searchWord, Long cursor, Pageable pageable) {
        List<Post> posts = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByTitleContainingOrContentContainingOrderByIdDesc(searchWord, searchWord, pageable)
                : postRepository.findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(cursor, searchWord, cursor, searchWord, pageable);

        return posts.stream()
                .map(PostDto::from).toList();
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getHotPostDtos(Pageable pageable) {
        Page<Post> posts = postRepository.findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc
                (NumberConstants.HOT_POSTS_BOUNDARY, LocalDateTime.now().minusMonths(1L), pageable);

        return posts.map(PostDto::from);
    }

    @Logging
    @Transactional
    public void updatePost(PostDto postDto) {
        postRepository.save(postDto.toEntity());
    }

    @Logging
    @Transactional
    public void deletePost(PostDto postDto) {
        postRepository.delete(postDto.toEntity());
        postLikeRepository.deleteByPostLikePK_PostId(postDto.id());

        List<Comment> comments = commentRepository.findByPost_Id(postDto.id());
        comments.forEach(
                comment -> {
                    commentRepository.delete(comment);
                    commentLikeRepository.deleteByCommentLikePK_CommentId(comment.getId());
                }
        );
    }

    @Transactional
    public void addViewCount(PostDto postDto) {
        postRepository.updateViewCount(postDto.id());
    }

    @Transactional(readOnly = true)
    public PostDto getPostDtoById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
        return PostDto.from(post);
    }

    public void isValidate(PostDto postDto, UserDto userDto) {
        if (!Objects.equals(postDto.userDto().id(), userDto.id()))
            throw new BusinessException(PostErrorCode.FORBIDDEN);
    }

    public int getNextPage(Page<PostDto> postDtos, int page) {
        return (postDtos.hasNext())
                ? page + 1
                : NumberConstants.LAST_PAGE;
    }
}
