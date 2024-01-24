package org.fastcampus.oruryclient.gym.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurydomain.gym.db.repository.GymLikeRepository;
import org.fastcampus.orurydomain.gym.db.repository.GymRepository;
import org.fastcampus.orurydomain.gym.dto.GymLikeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GymLikeService {
    private final GymLikeRepository gymLikeRepository;
    private final GymRepository gymRepository;

    @Transactional
    public void createGymLike(GymLikeDto gymLikeDto) {
        if (gymLikeRepository.existsById(gymLikeDto.gymLikePK())) return;

        gymLikeRepository.save(gymLikeDto.toEntity());
        gymRepository.increaseLikeCount(gymLikeDto.gymLikePK().getGymId());
    }

    @Transactional
    public void deleteGymLike(GymLikeDto gymLikeDto) {
        if (!gymLikeRepository.existsById(gymLikeDto.gymLikePK())) return;

        gymLikeRepository.delete(gymLikeDto.toEntity());
        gymRepository.decreaseLikeCount(gymLikeDto.gymLikePK().getGymId());
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long gymId) {
        return gymLikeRepository.existsByGymLikePK_UserIdAndGymLikePK_GymId(userId, gymId);
    }
}
