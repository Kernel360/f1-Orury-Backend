package org.orury.domain.review.domain.infrastructure;

import org.orury.domain.review.domain.ReviewReader;
import org.orury.domain.review.domain.entity.Review;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ReviewReaderImpl implements ReviewReader {
    @Override
    public List<Review> findByGymId(Long gymId) {
        return null;
    }

    @Override
    public boolean existsByUserIdAndGymId(Long userId, Long gymId) {
        return false;
    }

    @Override
    public List<Review> findByGymIdOrderByIdDesc(Long gymId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Review> findByGymIdAndIdLessThanOrderByIdDesc(Long gymId, Long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public List<Review> findByUserIdOrderByIdDesc(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Review> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long cursor, Pageable pageable) {
        return null;
    }
}
