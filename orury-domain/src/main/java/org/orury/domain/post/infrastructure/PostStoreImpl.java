package org.orury.client.post.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.post.PostStore;
import org.orury.domain.post.db.Post;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostStoreImpl implements PostStore {
    private final PostRepository postRepository;

    @Override
    public void updateViewCount(Long id) {
        postRepository.updateViewCount(id);
    }

    @Override
    public void increaseCommentCount(Long id) {

    }

    @Override
    public void decreaseCommentCount(Long id) {

    }

    @Override
    public void increaseLikeCount(Long id) {

    }

    @Override
    public void decreaseLikeCount(Long id) {

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
