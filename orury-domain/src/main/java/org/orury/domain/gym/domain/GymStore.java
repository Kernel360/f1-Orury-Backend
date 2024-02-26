package org.orury.domain.gym.domain;

import org.orury.domain.gym.domain.entity.GymLike;

public interface GymStore {
    void increaseLikeCount(Long gymId);

    void decreaseLikeCount(Long gymId);

    void increaseReviewCount(Long gymId);

    void decreaseReviewCount(Long gymId);

    void addTotalScore(Long gymId, float reviewScore);

    void subtractTotalScore(Long gymId, float reviewScore);

    void saveGymLike(GymLike gymLike);

    void deleteGymLike(GymLike gymLike);
}
