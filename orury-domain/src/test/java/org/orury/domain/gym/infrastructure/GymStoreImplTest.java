package org.orury.domain.gym.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.gym.domain.entity.GymLike;
import org.orury.domain.gym.domain.entity.GymLikePK;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Store] 암장 StoreImpl 테스트")
@ActiveProfiles("test")
class GymStoreImplTest {
    private GymStore gymStore;
    private GymRepository gymRepository;
    private GymLikeRepository gymLikeRepository;

    @BeforeEach
    void setUp() {
        gymRepository = mock(GymRepository.class);
        gymLikeRepository = mock(GymLikeRepository.class);

        gymStore = new GymStoreImpl(gymRepository, gymLikeRepository);
    }

    @Test
    @DisplayName("성공적으로 Gym의 reviewCount를 1만큼 늘리고, totalScore를 reviewScore만큼 더해야 한다.")
    void should_IncreaseReviewCountAndIncreaseTotalScore() {
        // given
        Long gymId = 1L;
        float reviewScore = 4.5f;

        // when
        gymStore.increaseReviewCountAndTotalScore(gymId, reviewScore);

        // then
        then(gymRepository).should(times(1))
                .increaseReviewCount(anyLong());
        then(gymRepository).should(times(1))
                .addTotalScore(anyLong(), anyFloat());
    }

    @Test
    @DisplayName("성공적으로 Gym의 totalScore를 oldScore만큼 빼고 newScore만큼 더해야 한다.")
    void should_UpdateTotalScore() {
        // given
        Long gymId = 2L;
        float oldScore = 3.5f;
        float newScore = 1f;

        // when
        gymStore.updateTotalScore(gymId, oldScore, newScore);

        // then
        then(gymRepository).should(times(1))
                .addTotalScore(anyLong(), anyFloat());
    }

    @Test
    @DisplayName("성공적으로 Gym의 reviewCount를 1만큼 줄이고, totalScore를 reviewScore만큼 빼야 한다.")
    void should_DecreaseReviewCountAndDecreaseTotalScore() {
        // given
        Long gymId = 3L;
        float reviewScore = 3.5f;

        // when
        gymStore.decreaseReviewCountAndTotalScore(gymId, reviewScore);

        // then
        then(gymRepository).should(times(1))
                .decreaseReviewCount(anyLong());
        then(gymRepository).should(times(1))
                .subtractTotalScore(anyLong(), anyFloat());
    }

    @Test
    @DisplayName("성공적으로 GymLike를 생성하고 Gym의 likeCount를 1만큼 늘려야 한다.")
    void should_CreateGymLikeAndIncreaseGymLIkeCount() {
        // given
        Long gymId = 1L;
        GymLike gymLike = createGymLike(gymId);

        // when
        gymStore.createGymLike(gymLike);

        // then
        then(gymLikeRepository).should(times(1))
                .save(any());
        then(gymRepository).should(times(1))
                .increaseLikeCount(anyLong());
    }

    @Test
    @DisplayName("성공적으로 GymLike를 삭제하고 Gym의 likeCount를 1만큼 줄여야 한다.")
    void should_DeleteGymLikeAndDecreaseGymLIkeCount() {
        // given
        Long gymId = 1L;
        GymLike gymLike = createGymLike(gymId);

        // when
        gymStore.deleteGymLike(gymLike);

        // then
        then(gymLikeRepository).should(times(1))
                .delete(any());
        then(gymRepository).should(times(1))
                .decreaseLikeCount(anyLong());
    }

    @Test
    @DisplayName("성공적으로 GymLike를 삭제하고 Gym의 likeCount를 1만큼 줄여야 한다.")
    void when_GymLikeByUserIdExists_Then_DeleteGymLikeAndDecreaseLikeCount() {
        // given
        Long userId = 5L;
        List<GymLike> gymLikes = List.of(
                createGymLike(1L),
                createGymLike(2L),
                createGymLike(3L),
                createGymLike(4L));

        given(gymLikeRepository.findByGymLikePK_UserId(userId))
                .willReturn(gymLikes);

        // when
        gymStore.deleteGymLikesByUserId(userId);

        // then
        then(gymLikeRepository).should(times(1))
                .findByGymLikePK_UserId(anyLong());
        then(gymRepository).should(times(gymLikes.size()))
                .decreaseLikeCount(anyLong());
        then(gymLikeRepository).should(times(gymLikes.size()))
                .delete(any());
    }

    private GymLike createGymLike(Long gymId) {
        return GymLike.of(GymLikePK.of(1L, gymId));
    }
}
