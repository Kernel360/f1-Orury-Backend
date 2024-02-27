package org.orury.domain.gym.infrastructure;

import org.orury.domain.gym.domain.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GymRepository extends JpaRepository<Gym, Long> {
    List<Gym> findByNameContaining(String searchWord);

    @Modifying
    @Query("UPDATE gym SET likeCount = likeCount + 1 WHERE id = :gymId")
    void increaseLikeCount(Long gymId);

    @Modifying
    @Query("UPDATE gym SET likeCount = likeCount - 1 WHERE id = :gymId")
    void decreaseLikeCount(Long gymId);

    @Modifying
    @Query("UPDATE gym SET reviewCount = reviewCount + 1 WHERE id = :gymId")
    void increaseReviewCount(Long gymId);

    @Modifying
    @Query("UPDATE gym SET reviewCount = reviewCount - 1 WHERE id = :gymId")
    void decreaseReviewCount(Long gymId);

    @Modifying
    @Query("UPDATE gym SET totalScore = totalScore + :reviewScore WHERE id = :gymId")
    void addTotalScore(@Param("gymId") Long gymId, @Param("reviewScore") float reviewScore);

    @Modifying
    @Query("UPDATE gym SET totalScore = totalScore - :reviewScore WHERE id = :gymId")
    void subtractTotalScore(@Param("gymId") Long gymId, @Param("reviewScore") float reviewScore);


}