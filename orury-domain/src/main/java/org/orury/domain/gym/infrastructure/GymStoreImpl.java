package org.orury.domain.gym.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.gym.domain.entity.GymLike;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GymStoreImpl implements GymStore {
    private final GymRepository gymRepository;
    private final GymLikeRepository gymLikeRepository;

    @Override
    public void increaseLikeCount(Long gymId) {
        gymRepository.increaseLikeCount(gymId);
    }

    @Override
    public void decreaseLikeCount(Long gymId) {
        gymRepository.decreaseLikeCount(gymId);
    }

    @Override
    public void increaseReviewCountAndTotalScore(Long gymId, float reviewScore) {
        gymRepository.increaseReviewCount(gymId);
        gymRepository.addTotalScore(gymId, reviewScore);
    }

    @Override
    public void updateTotalScore(Long gymId, float oldScore, float newScore) {
        gymRepository.addTotalScore(gymId, newScore - oldScore);
    }

    @Override
    public void decreaseReviewCountAndTotalScore(Long gymId, float reviewScore) {
        gymRepository.decreaseReviewCount(gymId);
        gymRepository.subtractTotalScore(gymId, reviewScore);
    }

    @Override
    public void saveGymLike(GymLike gymLike) {
        gymLikeRepository.save(gymLike);
    }

    @Override
    public void deleteGymLike(GymLike gymLike) {
        gymLikeRepository.delete(gymLike);
    }

    @Override
    public void deleteGymLikesByUserId(Long userId) {
        gymLikeRepository.findByGymLikePK_UserId(userId).forEach(
                gymLike -> {
                    gymRepository.decreaseLikeCount(gymLike.getGymLikePK().getGymId());
                    gymLikeRepository.delete(gymLike);
                }
        );
    }
}
