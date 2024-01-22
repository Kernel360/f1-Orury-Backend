package org.fastcampus.orurydomain.gym.db.repository;

import org.fastcampus.orurydomain.gym.db.model.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GymRepository extends JpaRepository<Gym, Long> {
    List<Gym> findByNameContaining(String searchWord);

    @Modifying
    @Query("UPDATE gym SET likeCount = likeCount + 1 WHERE id = :gymId")
    void increaseLikeCount(Long gymId);

    @Modifying
    @Query("UPDATE gym SET likeCount = likeCount - 1 WHERE id = :gymId")
    void decreaseLikeCount(Long gymId);
}