package org.orury.domain.gym;

import org.orury.domain.gym.db.model.Gym;
import org.orury.domain.gym.db.model.GymLike;
import org.orury.domain.gym.db.model.GymLikePK;

import java.util.List;

public interface GymReader {
    Gym findGymById(Long id);

    boolean existsGymById(Long id);

    List<Gym> findGymsBySearchWord(String searchWord);

    boolean existGymLikeById(GymLikePK gymLikePK);

    boolean existsGymLikeByUserIdAndGymId(Long userId, Long gymId);

    List<GymLike> findGymLikeByUserId(Long userId);
}
