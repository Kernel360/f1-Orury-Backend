package org.orury.domain.review.domain.infrastructure;

import org.orury.domain.review.domain.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByGymId(Long gymId);

    boolean existsByUserIdAndGymId(Long userId, Long gymId);

    List<Review> findByGymIdOrderByIdDesc(Long gymId, Pageable pageable);

    List<Review> findByGymIdAndIdLessThanOrderByIdDesc(Long gymId, Long cursor, Pageable pageable);

    List<Review> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<Review> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long cursor, Pageable pageable);

    @Modifying
    @Query("update review r set " +
            "r.thumbCount = CASE WHEN :reactionType = 1 THEN r.thumbCount + 1 ELSE r.thumbCount END, " +
            "r.interestCount = CASE WHEN :reactionType = 2 THEN r.interestCount + 1 ELSE r.interestCount END, " +
            "r.helpCount = CASE WHEN :reactionType = 3 THEN r.helpCount + 1 ELSE r.helpCount END, " +
            "r.likeCount = CASE WHEN :reactionType = 4 THEN r.likeCount + 1 ELSE r.likeCount END, " +
            "r.angryCount = CASE WHEN :reactionType = 5 THEN r.angryCount + 1 ELSE r.angryCount END " +
            "where r.id = :reviewId")
    void increaseReactionCount(@Param("reviewId") Long reviewId, @Param("reactionType") int reactionType);

    @Modifying
    @Query("update review r set " +
            "r.thumbCount = CASE WHEN :reactionType = 1 THEN r.thumbCount - 1 ELSE r.thumbCount END, " +
            "r.interestCount = CASE WHEN :reactionType = 2 THEN r.interestCount + 1 ELSE r.interestCount END, " +
            "r.helpCount = CASE WHEN :reactionType = 3 THEN r.helpCount - 1 ELSE r.helpCount END, " +
            "r.likeCount = CASE WHEN :reactionType = 4 THEN r.likeCount - 1 ELSE r.likeCount END, " +
            "r.angryCount = CASE WHEN :reactionType = 5 THEN r.angryCount - 1 ELSE r.angryCount END " +
            "where r.id = :reviewId")
    void decreaseReactionCount(@Param("reviewId") Long reviewId, @Param("reactionType") int reactionType);

    @Modifying
    @Query("update review r set " +
            "r.thumbCount = CASE WHEN :oldReactionType = 1 THEN r.thumbCount - 1 WHEN :newReactionType = 1 THEN r.thumbCount + 1 ELSE r.thumbCount END, " +
            "r.interestCount = CASE WHEN :oldReactionType = 2 THEN r.interestCount - 1 WHEN :newReactionType = 2 THEN r.interestCount + 1 ELSE r.interestCount END, " +
            "r.helpCount = CASE WHEN :oldReactionType = 3 THEN r.helpCount - 1 WHEN :newReactionType = 3 THEN r.helpCount + 1 ELSE r.helpCount END, " +
            "r.likeCount = CASE WHEN :oldReactionType = 4 THEN r.likeCount - 1 WHEN :newReactionType = 4 THEN r.likeCount + 1 ELSE r.likeCount END, " +
            "r.angryCount = CASE WHEN :oldReactionType = 5 THEN r.angryCount - 1 WHEN :newReactionType = 5 THEN r.angryCount + 1 ELSE r.angryCount END " +
            "where r.id = :reviewId")
    void updateReactionCount(@Param("reviewId") Long reviewId, @Param("oldReactionType") int oldReactionType, @Param("newReactionType") int newReactionType);
}
