package org.orury.domain.gym.db.repository;

import org.orury.domain.gym.db.model.GymLikePK;
import org.orury.domain.gym.db.model.GymLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GymLikeRepository extends JpaRepository<GymLike, GymLikePK> {
    boolean existsByGymLikePK_UserIdAndGymLikePK_GymId(Long userId, Long gymId);

    List<GymLike> findByGymLikePK_UserId(Long userId);
}
