package org.orury.domain.review.db.repository;

import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewReactionRepository extends JpaRepository<ReviewReaction, ReviewReactionPK> {

    List<ReviewReaction> findByReviewReactionPK_UserId(Long userId);

}
