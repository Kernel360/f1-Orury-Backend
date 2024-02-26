package org.orury.domain.gym.domain;

import org.orury.domain.gym.domain.entity.GymLike;

public interface GymStore {
    void increaseLikeCount(Long gymId);

    void decreaseLikeCount(Long gymId);

    void increaseReviewCountAndTotalScore(Long gymId, float reviewScore);

    void updateTotalScore(Long gymId, float oldScore, float newScore);

    void decreaseReviewCountAndTotalScore(Long gymId, float reviewScore);

    void saveGymLike(GymLike gymLike);

    void deleteGymLike(GymLike gymLike);

    void deleteGymLikesByUserId(Long userId);
}
