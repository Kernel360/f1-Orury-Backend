package org.orury.domain.post.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.post.domain.entity.PostLike;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostStoreImpl implements PostStore {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

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
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }
}
