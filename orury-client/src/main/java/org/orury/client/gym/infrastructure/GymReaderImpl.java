package org.orury.client.gym.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.common.error.code.GymErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.gym.GymReader;
import org.orury.domain.gym.db.model.Gym;
import org.orury.domain.gym.db.model.GymLike;
import org.orury.domain.gym.db.model.GymLikePK;
import org.orury.domain.gym.db.repository.GymLikeRepository;
import org.orury.domain.gym.db.repository.GymRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GymReaderImpl implements GymReader {
    private final GymRepository gymRepository;
    private final GymLikeRepository gymLikeRepository;

    @Override
    public Gym findGymById(Long id) {
        return gymRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GymErrorCode.NOT_FOUND));
    }

    @Override
    public boolean existsGymById(Long id) {
        return gymRepository.existsById(id);
    }

    @Override
    public List<Gym> findGymsBySearchWord(String searchWord) {
        return gymRepository.findByNameContaining(searchWord);
    }

    @Override
    public boolean existGymLikeById(GymLikePK gymLikePK) {
        return gymLikeRepository.existsById(gymLikePK);
    }

    @Override
    public boolean existsGymLikeByUserIdAndGymId(Long userId, Long gymId) {
        return gymLikeRepository.existsByGymLikePK_UserIdAndGymLikePK_GymId(userId, gymId);
    }

    @Override
    public List<GymLike> findGymLikeByUserId(Long userId) {
        return gymLikeRepository.findByGymLikePK_UserId(userId);
    }
}
