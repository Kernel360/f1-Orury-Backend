package org.fastcampus.orurydomain.review.db.repository;

import org.fastcampus.orurydomain.review.db.model.ReviewReaction;
import org.fastcampus.orurydomain.review.db.model.ReviewReactionPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewReactionRepository extends JpaRepository<ReviewReaction, ReviewReactionPK> {

    List<ReviewReaction> findByReviewReactionPK_UserId(Long userId);

}
