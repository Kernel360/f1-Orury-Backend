package org.orury.domain.gym.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.gym.domain.GymReader;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.gym.domain.entity.GymLikePK;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GymReaderImpl implements GymReader {
    private final GymRepository gymRepository;
    private final GymLikeRepository gymLikeRepository;

    @Override
    public Optional<Gym> findGymById(Long id) {
        return gymRepository.findById(id);
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
    public boolean existsGymLikeById(GymLikePK gymLikePK) {
        return gymLikeRepository.existsById(gymLikePK);
    }

    @Override
    public boolean existsGymLikeByUserIdAndGymId(Long userId, Long gymId) {
        return gymLikeRepository.existsByGymLikePK_UserIdAndGymLikePK_GymId(userId, gymId);
    }
}
