package org.orury.domain.post.infrastructure;

import org.orury.common.util.S3Folder;
import org.orury.domain.global.domain.ImageUtils;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.post.domain.entity.PostLike;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostStoreImpl implements PostStore {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final ImageUtils imageUtils;

    @Override
    public void save(PostLike postLike) {
        postLikeRepository.save(postLike);
        postRepository.increaseLikeCount(postLike.getPostLikePK().getPostId());
    }

    @Override
    public void delete(PostLike postLike) {
        postLikeRepository.delete(postLike);
        postRepository.decreaseLikeCount(postLike.getPostLikePK().getPostId());
    }

    @Override
    public void updateViewCount(Long id) {
        postRepository.updateViewCount(id);
    }

    @Override
    public void deletePostLikesByUserId(Long id) {
        postLikeRepository.findByPostLikePK_UserId(id)
                .forEach(
                        postLike -> {
                            postRepository.decreaseCommentCount(postLike.getPostLikePK()
                                    .getPostId());
                            postLikeRepository.delete(postLike);
                        }
                );
    }

    @Override
    public void deletePostsByUserId(Long id) {
        postRepository.findByUserId(id)
                .forEach(
                        post -> {
                            imageUtils.oldS3ImagesDelete(S3Folder.POST.getName(), post.getImages());
                            postRepository.delete(post);
                        }
                );
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }
}
