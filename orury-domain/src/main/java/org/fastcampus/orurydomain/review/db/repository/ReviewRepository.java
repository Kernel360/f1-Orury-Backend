package org.fastcampus.orurydomain.review.db.repository;

import org.fastcampus.orurydomain.review.db.model.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUser_IdAndGym_Id(Long userId, Long gymId);

    List<Review> findByGymIdOrderByIdDesc(Long gymId, Pageable pageable);

    List<Review> findByGymIdAndIdLessThanOrderByIdDesc(Long gymId, Long cursor, Pageable pageable);
}
