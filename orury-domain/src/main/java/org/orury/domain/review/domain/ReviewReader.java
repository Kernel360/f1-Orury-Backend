package org.orury.domain.review.domain;

import org.orury.domain.review.domain.entity.Review;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewReader {
    List<Review> findByGymId(Long gymId);

    boolean existsByUserIdAndGymId(Long userId, Long gymId);

    List<Review> findByGymIdOrderByIdDesc(Long gymId, Pageable pageable);

    List<Review> findByGymIdAndIdLessThanOrderByIdDesc(Long gymId, Long cursor, Pageable pageable);

    List<Review> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<Review> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long cursor, Pageable pageable);
}
