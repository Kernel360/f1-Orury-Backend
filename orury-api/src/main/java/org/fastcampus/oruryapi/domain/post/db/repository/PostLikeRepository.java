package org.fastcampus.oruryapi.domain.post.db.repository;

import org.fastcampus.oruryapi.domain.post.db.model.PostLike;
import org.fastcampus.oruryapi.domain.post.db.model.PostLikePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikePK> {
    Optional<PostLike> findByPostLikePK_UserIdAndPostLikePK_PostId(Long userId, Long postId);

    int countByPostLikePK_PostId(Long postId);

    boolean existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(Long userId, Long postId);
}
