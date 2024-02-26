package org.orury.client.gym.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.gym.GymStore;
import org.orury.domain.gym.db.model.GymLike;
import org.orury.domain.gym.db.repository.GymLikeRepository;
import org.orury.domain.gym.db.repository.GymRepository;
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
    public void increaseReviewCount(Long gymId) {
        gymRepository.increaseReviewCount(gymId);
    }

    @Override
    public void decreaseReviewCount(Long gymId) {
        gymRepository.decreaseReviewCount(gymId);
    }

    @Override
    public void addTotalScore(Long gymId, float reviewScore) {
        gymRepository.addTotalScore(gymId, reviewScore);
    }

    @Override
    public void subtractTotalScore(Long gymId, float reviewScore) {
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
}
