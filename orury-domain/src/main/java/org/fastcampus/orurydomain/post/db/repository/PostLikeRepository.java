package org.fastcampus.orurydomain.post.db.repository;

import org.fastcampus.orurydomain.post.db.model.PostLike;
import org.fastcampus.orurydomain.post.db.model.PostLikePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikePK> {
    boolean existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(Long userId, Long postId);

    boolean existsByPostLikePK(PostLikePK postLikePK);

    List<PostLike> findByPostLikePK_UserId(Long userId);
}
