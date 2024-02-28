package org.orury.domain.review.domain.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.ReviewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.review.domain.ReviewReader;
import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewReaderImpl implements ReviewReader {
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;

    @Override
    public List<Review> findByGymId(Long gymId) {
        return reviewRepository.findByGymId(gymId);
    }

    @Override
    public boolean existsByUserIdAndGymId(Long userId, Long gymId) {
        return reviewRepository.existsByUserIdAndGymId(userId, gymId);
    }

    @Override
    public List<Review> findByGymIdOrderByIdDesc(Long gymId, Pageable pageable) {
        return reviewRepository.findByGymIdOrderByIdDesc(gymId, pageable);
    }

    @Override
    public List<Review> findByGymIdAndIdLessThanOrderByIdDesc(Long gymId, Long cursor, Pageable pageable) {
        return reviewRepository.findByGymIdAndIdLessThanOrderByIdDesc(gymId, cursor, pageable);
    }

    @Override
    public List<Review> findByUserIdOrderByIdDesc(Long userId, Pageable pageable) {
        return reviewRepository.findByUserIdOrderByIdDesc(userId, pageable);
    }

    @Override
    public List<Review> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long cursor, Pageable pageable) {
        return reviewRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
    }

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ReviewErrorCode.NOT_FOUND));
    }

    @Override
    public Optional<ReviewReaction> findById(ReviewReactionPK reviewReactionPK) {
        return reviewReactionRepository.findById(reviewReactionPK);
    }

    @Override
    public List<ReviewReaction> findReviewReactionsByUserId(Long userId) {
        return reviewReactionRepository.findByReviewReactionPK_UserId(userId);
    }
}
