package org.orury.domain.review.domain;

import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewReader {
    List<Review> findByGymId(Long gymId);

    boolean existsByUserIdAndGymId(Long userId, Long gymId);

    List<Review> findByGymIdOrderByIdDesc(Long gymId, Pageable pageable);

    List<Review> findByGymIdAndIdLessThanOrderByIdDesc(Long gymId, Long cursor, Pageable pageable);

    List<Review> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<Review> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long cursor, Pageable pageable);

    Optional<Review> findById(Long id);

    Optional<ReviewReaction> findById(ReviewReactionPK reviewReactionPK);

    List<ReviewReaction> findReviewReactionsByUserId(Long userId);
}
