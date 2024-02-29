package org.orury.domain.gym.domain;

import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.gym.domain.entity.GymLikePK;

import java.util.List;
import java.util.Optional;

public interface GymReader {
    Optional<Gym> findGymById(Long id);

    boolean existsGymById(Long id);

    List<Gym> findGymsBySearchWord(String searchWord);

    boolean existsGymLikeById(GymLikePK gymLikePK);

    boolean existsGymLikeByUserIdAndGymId(Long userId, Long gymId);
}
